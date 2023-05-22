package edu.bhcc.SuperBudget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {

    private final BudgetCategoryRepository budgetCategoryRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public BudgetService(
            BudgetCategoryRepository budgetCategoryRepository,
            TransactionRepository transactionRepository
    ) {
        this.budgetCategoryRepository = budgetCategoryRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<BudgetCategory> findAllBudgetCategories() {
        return budgetCategoryRepository.findAll();
    }

    public void deleteBudgetCategory(Long id) {
        budgetCategoryRepository.deleteById(id);
    }

    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<BudgetCategory> getAllBudgetCategories() {
        return budgetCategoryRepository.findAll();
    }

    public BudgetCategory saveBudgetCategory(BudgetCategory budgetCategory) {
        return budgetCategoryRepository.save(budgetCategory);
    }

    public BudgetCategory updateBudgetCategory(BudgetCategory budgetCategory) {
        return budgetCategoryRepository.save(budgetCategory);
    }

    public BudgetCategory findBudgetCategoryById(Long id) {
        return budgetCategoryRepository.findById(id).orElse(null);
    }

    public void saveTransaction(Transaction transaction) {
        BudgetCategory budgetCategory = transaction.getBudgetCategory();
        if (budgetCategory != null) {
            budgetCategory.updateBalanceAndActivity(transaction.getAmount());
            transactionRepository.save(transaction);
            budgetCategoryRepository.save(budgetCategory);
        } else {
            throw new IllegalArgumentException("Budget category is required for the transaction.");
        }
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction != null) {
            BudgetCategory budgetCategory = transaction.getBudgetCategory();
            if (budgetCategory != null) {
                Double remainingAmount = budgetCategory.getRemainingAmount() + transaction.getAmount();
                Double newBalance = budgetCategory.getBalance() + transaction.getAmount();

                budgetCategory.setRemainingAmount(remainingAmount);
                budgetCategory.setBalance(newBalance);
                budgetCategoryRepository.save(budgetCategory);
            }
            transactionRepository.delete(transaction);
        }
    }

    public void updateTransaction(Long id, Double newAmount) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction != null) {
            BudgetCategory budgetCategory = transaction.getBudgetCategory();
            if (budgetCategory != null) {
                Double remainingAmount = budgetCategory.getRemainingAmount()
                        + transaction.getAmount() - newAmount;
                Double newBalance = budgetCategory.getBalance()
                        + transaction.getAmount() - newAmount;

                budgetCategory.setRemainingAmount(remainingAmount);
                budgetCategory.setBalance(newBalance);
                budgetCategoryRepository.save(budgetCategory);
            }
            transaction.setAmount(newAmount);
            transactionRepository.save(transaction);
        }
    }

    public BudgetCategory updateBudgetCategory(BudgetCategory budgetCategory, Double previousAllocation) {
        Double allocationChange = budgetCategory.getAllocation() - previousAllocation;
        Double newRemainingAmount = budgetCategory.getRemainingAmount() + allocationChange;
        Double newBalance = budgetCategory.getBalance() + allocationChange;

        budgetCategory.setRemainingAmount(newRemainingAmount);
        budgetCategory.setBalance(newBalance);

        return budgetCategoryRepository.save(budgetCategory);
    }
}
