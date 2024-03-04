package com.shaheen.customercrud.dao;

import com.shaheen.customercrud.entity.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        String sql = """
                SELECT * FROM customer
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        String sql = """
                SELECT * FROM customer WHERE id=?
                """;
        var res = jdbcTemplate.query(sql ,customerRowMapper ,id)
                .stream()
                .findFirst();
        return res;
    }

    @Override
    public void insertCustomer(Customer customer) {
        String sql = """
                INSERT INTO customer(name ,email ,age)
                VALUES (?, ?, ?)
                """;
        int result = jdbcTemplate.update(sql ,customer.getName()
                                             ,customer.getEmail()
                                             ,customer.getAge());
        System.out.println("JDBC query : " + result);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        String sql = """
                SELECT COUNT(*) FROM customer WHERE email=?
                """;
        Integer res = jdbcTemplate.queryForObject(sql,Integer.class ,email);
        return res != null && res > 0;
    }

    @Override
    public void deleteCustomerById(Integer id) {
        String sql = """
                DELETE FROM customer WHERE id=?
                """;
        int res = jdbcTemplate.update(sql ,id);
        System.out.println("JDBC query : " + res);
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        String sql = """
                SELECT COUNT(*) FROM customer WHERE id=?
                """;
        Integer res = jdbcTemplate.queryForObject(sql,Integer.class ,id);
        return res != null && res > 0;
    }

    @Override
    public void updateCustomer(Customer customer) {
        if(customer.getName() != null){
           String sql = """
                   UPDATE customer SET name=? WHERE id=?
                   """;
           int res = jdbcTemplate.update(sql ,customer.getName(),customer.getId());
           System.out.println("update customer name result = " + res);
        }
        if(customer.getEmail() != null){
            String sql = """
                   UPDATE customer SET email=? WHERE id=?
                   """;
            int res = jdbcTemplate.update(sql ,customer.getEmail(),customer.getId());
            System.out.println("update customer email result = " + res);
        }
        if(customer.getAge() != null){
            String sql = """
                   UPDATE customer SET age=? WHERE id=?
                   """;
            int res = jdbcTemplate.update(sql ,customer.getAge(),customer.getId());
            System.out.println("update customer age result = " + res);
        }
    }
}
