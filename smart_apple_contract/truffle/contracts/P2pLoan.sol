// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract P2pLoan {

    uint public total = 0;

    mapping(uint => LoanTerm) public loanMap;
    mapping(uint => LenderTerm) public lenderMap;
    mapping(uint => uint) public paymentMap;

    struct LoanTerm {
        address issuer;
        uint maxAmount;
        uint rate;
        uint terms;
        uint amount;
        uint payRound;
    }

    struct LenderTerm {
        uint loanId;
        address lender;
        uint amount;
    }

    struct PaymentTerm {
        uint loanId;
        address lender;
        uint amount;
    }

    event NewLoan(uint indexed loanId,address indexed issuer, uint maxAmount, uint rate, uint terms);
    event SubLoan(uint indexed loanId,address indexed lender, uint amount, uint subIndex);

    event Payment(uint indexed loanId,uint interest, uint principal, uint payRound);
    event PaymentReceive(uint indexed loanId,address indexed to, uint interest, uint principal ,uint payRound);


    constructor() {
    }

    function registerLoan(uint maxAmount, uint rate, uint terms) public returns (uint) {
        LoanTerm memory loan = LoanTerm(msg.sender,maxAmount,rate,terms, maxAmount,0);
        
        total++;
        loanMap[total] = loan;
        emit NewLoan(total,msg.sender, loan.rate,loan.terms, maxAmount);
        return total;
    }

    function takeLoan(uint loanId) public {
        require(loanId<=total,"out_of_index");
        require(lenderMap[loanId].lender == address(0),"loan_taked");
        LoanTerm storage loan = loanMap[loanId];
        LenderTerm memory lender = LenderTerm(loanId, msg.sender, loan.amount);
        lenderMap[loanId] = lender;
        emit SubLoan(loanId,msg.sender,loan.amount, 1);
        //todo send token
    }

    function makePayment(uint loanId) public {
        require(loanId<=total,"out_of_index");
        LoanTerm storage loan = loanMap[loanId];
        
        require(msg.sender == loan.issuer,"!issuer");
        LenderTerm memory lender = lenderMap[loanId];
        require(lender.amount>0,"no_lender");
        require(loan.payRound < loan.terms,"loanClosed");
        loan.payRound = loan.payRound +1;

        uint interest = lender.amount*loan.rate/100/loan.terms;
        uint principal = lender.amount/loan.terms;
        emit PaymentReceive( loanId, lender.lender,interest, principal ,loan.payRound);
    }

    // function getLoans() public view returns (LoanTerm[] memory){
    //     return loanList;
    // }

}