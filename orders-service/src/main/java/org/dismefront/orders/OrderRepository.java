package org.dismefront.orders;

import org.dismefront.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Order findByOrderUUID(String orderUUID);
    List<Order> findAllByStatus(OrderStatus status);
}
