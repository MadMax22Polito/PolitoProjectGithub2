package com.example.jaumenartbaron.politoproject;

import java.util.List;

/**
 * Created by Jaume Nart Baron on 04/05/2017.
 */

public class ExpenseInformation
{
        String name;
        double cost;
        List<String> ExpenseUsers;

    public ExpenseInformation()
    {

    }

    public ExpenseInformation(String name, double cost,List<String> expenseUsers) {
        this.name = name;
        this.cost = cost;
        ExpenseUsers = expenseUsers;
    }

    /*public String getExpenseName() {
        return ExpenseName;
    }

    public String getExpenseCost() {
        return ExpenseCost;
    }

    public List<String> getExpenseUsers() {
        return ExpenseUsers;
    }*/
}
