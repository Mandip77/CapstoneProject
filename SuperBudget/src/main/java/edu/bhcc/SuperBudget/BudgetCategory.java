package edu.bhcc.SuperBudget;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "budget_category")
public class BudgetCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double allocation;
    private Double balance;
    private Double activity;

    @Column(name = "remaining_amount")
    private Double remainingAmount = 0.0; // Set default value to 0.0

    @OneToMany(mappedBy = "budgetCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    public BudgetCategory() {
    }

    public BudgetCategory(String name, Double allocation, Double balance) {
        this.name = name;
        this.allocation = allocation;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAllocation() {
        return allocation;
    }

    public void setAllocation(Double allocation) {
        this.allocation = allocation;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Double getActivity() {
        return activity;
    }

    public void setActivity(Double activity) {
        this.activity = activity;
    }

    public void updateBalanceAndActivity(Double transactionAmount) {
        if (transactionAmount > this.balance) {
            throw new IllegalArgumentException("Transaction amount exceeds the remaining budget.");
        }

        this.balance -= transactionAmount;
        this.activity = transactionAmount;
    }
}
