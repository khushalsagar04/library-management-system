package com.example.demo.service;

import com.example.demo.dto.InitiateTransactionRequest;
import com.example.demo.models.*;
import com.example.demo.repository.TransactionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {

    @Autowired
    StudentService studentService;

    @Autowired
    BookService bookService;
    @Autowired
    AdminService adminService;
    @Autowired
    TransactionRepository transactionRepository;
    @Value("${student.allowed.max.books}")
    Integer maxBooksAllowed;
    @Value("${student.allowed.duration}")
    Integer allowedDuration;

    public String initiateTransaction(InitiateTransactionRequest initiateTransactionRequest) throws Exception {
        if(initiateTransactionRequest.getTransactionType().equals(TransactionType.ISSUE)){
            return issueBook(initiateTransactionRequest);
        }else{
            return returnBook(initiateTransactionRequest);
        }


//        Another way to do the above thing

//        return initiateTransactionRequest.getTransactionType() == TransactionType.ISSUE
//                ? issueBook(initiateTransactionRequest)
//                : returnBook(initiateTransactionRequest);
    }

    /*
    * Issue of a Book
    *   1- Validate the request -> if the book is available or not, student and book is valid or not
    *   2- Validate if the book can be issued -> We need to check if the student has available limit (issue limit) in his account or not
    *   3- Entry in transaction
    *   4- Book to be assigned to a student -> update student column in the book table
     */
    private String issueBook(InitiateTransactionRequest initiateTransactionRequest) throws Exception{
        List<Student> studentList = studentService.findStudentById(initiateTransactionRequest.getStudentId());
        Student student = studentList.size() > 0 ? studentList.get(0) : null;

        List<Book> bookList = bookService.findBook("id", String.valueOf(initiateTransactionRequest.getBookId()));
        Book book = bookList.size() > 0 ? bookList.get(0) : null;

        Admin admin = adminService.findAdminById(initiateTransactionRequest.getAdminId());

//        Validate the request
        if(student == null || book == null || admin == null){
            throw new Exception("Invalid Request");
        }
//        Validate if book is available
        if(book.getStudent() != null){
            throw new Exception("Book already issued");
        }

//        Validate if the book can be issued to the given student
        if(student.getBookList().size() >= maxBooksAllowed){
            throw new Exception("You reached maximum book issue limit");
        }

        Transaction transaction = null;
        try {
//        Entry in transaction table

            transaction = Transaction.builder()
                    .transactionId(UUID.randomUUID().toString())
                    .student(student)
                    .book(book)
                    .admin(admin)
                    .transactionStatus(TransactionStatus.PENDING)
                    .transactionType(TransactionType.ISSUE)
                    .build();

            transactionRepository.save(transaction);

//        Book to be assigned to a student
            book.setStudent(student);
            bookService.createBook(book);

            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        }catch(Exception e){
            transaction.setTransactionStatus(TransactionStatus.FAILURE);
            book.setStudent(null);
        }
        finally {
            transactionRepository.save(transaction);
        }
        return transaction.getTransactionId();

    }

    /*
    * 1- Validate the book, student, admin
    * 2- Entry in transaction table
    * 3- Due date check, if due date - issue date > allowedDuration => fineCalculation
    * 4- If there's no fine, de-allocate the book from student's name => book table
     */
    private String returnBook(InitiateTransactionRequest initiateTransactionRequest) throws Exception {
        List<Student> studentList = studentService.findStudentById(initiateTransactionRequest.getStudentId());
        Student student = studentList.size() > 0 ? studentList.get(0) : null;

        List<Book> bookList = bookService.findBook("id", String.valueOf(initiateTransactionRequest.getBookId()));
        Book book = bookList.size() > 0 ? bookList.get(0) : null;

        Admin admin = adminService.findAdminById(initiateTransactionRequest.getAdminId());

        if(student == null || book == null || admin == null){
            throw new Exception("Invalid Request");
        }
        if(book.getStudent() == null || !book.getStudent().getId().equals(student.getId())){
            throw new Exception("This book isn't assigned to the particular student");
        }

//        Get the corresponding issuance transaction
        Transaction issuanceTransaction = transactionRepository.findTopByStudentAndBookAndTransactionTypeOrderByIdDesc(student, book, TransactionType.ISSUE);
        if(issuanceTransaction == null){
            throw  new Exception("This book is not issued to anyone");
        }
        Transaction transaction = null;
        try{
            Integer fine = fineCalculation(issuanceTransaction.getCreatedOn());
           transaction = Transaction.builder()
                   .transactionId(UUID.randomUUID().toString())
                   .student(student)
                   .book(book)
                   .admin(admin)
                   .transactionStatus(TransactionStatus.PENDING)
                   .transactionType(TransactionType.RETURN)
                   .fine(fine)
                   .build();

           transactionRepository.save(transaction);

//           pay fine
           if(fine == 0){
               book.setStudent(null);
               bookService.createBook(book);
               transaction.setTransactionStatus(TransactionStatus.SUCCESS);
           }else{
               payFine(fine, student.getId(), transaction.getTransactionId());
               book.setStudent(null);
               bookService.createBook(book);
               transaction.setTransactionStatus(TransactionStatus.SUCCESS);
           }

        }catch (Exception e){
            transaction.setTransactionStatus(TransactionStatus.FAILURE);
            book.setStudent(student);
        }finally {
            transactionRepository.save(transaction);
        }

        return transaction.getTransactionId();
    }

    private Integer fineCalculation(Date issuanceTime){
        long issuanceTimeInMillis = issuanceTime.getTime();
        long currentTime = System.currentTimeMillis();

        long diff = currentTime - issuanceTimeInMillis;

        long daysPassed = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        if(daysPassed > allowedDuration){
            return (int)(daysPassed - allowedDuration) * 10;
        }
        return 0;
    }
    public void payFine(Integer amount, Integer StudentId, String txnId){

    }
}
