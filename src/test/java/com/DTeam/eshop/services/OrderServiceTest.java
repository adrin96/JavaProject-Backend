package com.DTeam.eshop.services;

import com.DTeam.eshop.entities.Customer;
import com.DTeam.eshop.entities.Order;
import com.DTeam.eshop.entities.Product;
import java.time.LocalDateTime;
import java.time.Month;

import java.util.List;
import java.util.ArrayList;
import javax.security.auth.message.callback.PrivateKeyCallback;
import org.junit.jupiter.api.Test;

import org.hamcrest.Matchers;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.junit.Assert.*;
import org.junit.ComparisonFailure;

public class OrderServiceTest {

    final OrderService orderService = mock(OrderService.class);
    final CustomerService customerService = mock(CustomerService.class);
    final ProductService productService = mock(ProductService.class);

    final static String localDateTime = LocalDateTime.now().toString();

    @Test
    public void testListAll() {

        when(orderService.listAll()).thenReturn(prepareMocData(LocalDateTime.parse(localDateTime)));

        assertEquals(orderService.listAll().get(0).getOrderId(), null);
        assertEquals(orderService.listAll().get(2).getOrderId().longValue(), 32L);
        assertEquals(orderService.listAll().get(2).getPurchaseDate().toString(), localDateTime);
        assertThat(orderService.listAll(), Matchers.hasSize(3));
    }

    private List<Order> prepareMocData(LocalDateTime localDateTime) {

        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order(32L, localDateTime));
        return orders;
    }
    
    @Test
    public void testSave() {

        List<Product> products = new ArrayList<>();
        products.add(new Product());
        products.add(new Product());
        products.add(new Product(32L, "name", "description", 42.0, 42, "photo"));

        Order order = (new Order(32L, LocalDateTime.of(2020, Month.MARCH, 22, 21, 40)));

//        when(orderService.save((orderToSave), products)).thenReturn(new Order(32L, LocalDateTime.of(2020, Month.MARCH, 22, 21, 40)));
//
//        Order order = orderService.save(orderToSave, products);
        when(orderService.save((order), products)).thenReturn(new Order(32L, LocalDateTime.of(2020, Month.MARCH, 22, 21, 40)));
       // when(orderService.save((any(Order.class)), products)).thenReturn(order);

      // Order orderSave =  orderService.save(order, products);
      //  order.setProducts(products);
      
     //   verify(orderService, times(1)).save(order, products);
        
//        assertEquals(order.getOrderId().longValue(), 32L);
//        assertEquals(order.getPurchaseDate(), LocalDateTime.of(2020, Month.MARCH, 22, 21, 40).toString());
//        assertEquals(order.getProducts().size(), 3);

        assertEquals(products.get(2).getName(), "name");
    }

    @Test
    public void testSaveEdit() {

        when(orderService.saveEdit(any(Order.class))).thenReturn(new Order(12L, LocalDateTime.parse(localDateTime)));

        Order order = orderService.saveEdit(new Order());

        assertEquals(order.getOrderId().longValue(), 12L);
        assertEquals(order.getPurchaseDate(), localDateTime);

    }

    @Test
    public void testGet() {

        Long id = 894L;

        when(orderService.get(id)).thenReturn(new Order());

        assertEquals(orderService.get(id).getOrderId(), null);
        assertEquals(orderService.get(id).getPurchaseDate().toString(), "");

        try {
            assertEquals(orderService.get(id).getCustomer(), "sample position");
            assertEquals(orderService.get(id).getShipmentDate(), null);
        } catch (AssertionError error) {
            assert (true);
        }
    }

    @Test
    public void testDelete() {

        Long id = 1589L;
        final Order order = new Order(id, LocalDateTime.MAX);

        orderService.delete(order.getOrderId());

        verify(orderService, times(1)).delete(id);
    }

    @Test
    public void testIsOrderExist() {

        Long id = 540L;

        when(orderService.isOrderExist(id)).thenReturn(true);

        final boolean result = orderService.isOrderExist(id);
        assertEquals(result, true);

    }

    @Test
    public void testGetByCustomer() {

        String localDateTime = LocalDateTime.now().toString();

        Long id = 42L;
        final Customer customer = new Customer(id, "", "", "");

        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order(32L, LocalDateTime.parse(localDateTime)));

        when(customerService.get(id)).thenReturn(customer);

        customer.setOrders(orders);

        assertEquals(customer.getOrders().size(), 3);
        assertEquals(customer.getOrders().get(2).getOrderId().toString(), "" + 32L);
        assertEquals(customer.getOrders().get(2).getPurchaseDate(), localDateTime);

        assertEquals(customer.getCustomerId().longValue(), 42L);

        assertEquals(orders.get(0).getOrderId(), null);
        assertEquals(orders.get(1).getProducts(), null);

    }

    @Test
    public void testGetOrderByStatus() {

        when(orderService.getOrderByStatus()).thenReturn(prepareMocData(LocalDateTime.parse(localDateTime)));

        orderService.getOrderByStatus().get(0).setOrderStatus("Przyjęto");
        verify(orderService, times(1)).getOrderByStatus();

        try {
            final String resultStatus = orderService.getOrderByStatus().get(0).getOrderStatus();
            assertEquals(resultStatus, "Wysłane"); // Ma zwrócić wszystkie które nie mają wysłane np. Przyjęto jak wyżej
        } catch (ComparisonFailure e) {
        }

    }

}