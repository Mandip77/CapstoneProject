package edu.bhcc.SuperBudget;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "budget_category_id")
    private BudgetCategory budgetCategory;

    public Transaction() {
    }

    public Transaction(String description, Double amount, BudgetCategory budgetCategory) {
        this.description = description;
        this.amount = amount;
        this.budgetCategory = budgetCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public BudgetCategory getBudgetCategory() {
        return budgetCategory;
    }

    public void setBudgetCategory(BudgetCategory budgetCategory) {
        if (this.budgetCategory != null) {
            this.budgetCategory.getTransactions().remove(this);
        }

        this.budgetCategory = budgetCategory;

        if (budgetCategory != null && !budgetCategory.getTransactions().contains(this)) {
            budgetCategory.getTransactions().add(this);
        }
    }
}
