package com.shaheen.customercrud;

import com.shaheen.customercrud.dao.CustomerRowMapper;
import com.shaheen.customercrud.entity.Customer;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // given
        CustomerRowMapper underTest = new CustomerRowMapper();
        ResultSet resultMock = mock(ResultSet.class);
        when(resultMock.getInt("id")).thenReturn(1);
        when(resultMock.getString("name")).thenReturn("Abdelrahman");
        when(resultMock.getString("email")).thenReturn("shaheen.com");
        when(resultMock.getInt("age")).thenReturn(24);
        // when
        Customer actual = underTest.mapRow(resultMock ,1);
        // then
        Customer expected = new Customer(1 ,"Abdelrahman" ,"shaheen.com" ,24);
        assertThat(actual).isEqualTo(expected);

    }
}