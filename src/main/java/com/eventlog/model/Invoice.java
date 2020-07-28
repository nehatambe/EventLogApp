package com.eventlog.model;

import com.eventlog.enums.Currency;
import com.eventlog.enums.Status;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name = "INVOICE")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Invoice implements Serializable {

  @Id
  @Column(name = "ID")
  private String id;

  @Column(name = "AMOUNT")
  private BigDecimal amount;

  @Column(name = "CURRENCY")
  @Enumerated(EnumType.STRING)
  private Currency currency;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "ITEMS")
  private Integer items;

  public Invoice() {}

  public Invoice(String id, BigDecimal amount, Currency currency, Status status, Integer items) {
    this.id = id;
    this.amount = amount;
    this.currency = currency;
    this.status = status;
    this.items = items;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public Integer getItems() {
    return items;
  }

  public void setItems(Integer items) {
    this.items = items;
  }

  @Override
  public String toString() {
    return "Invoice{"
        + "invoiceId='"
        + id
        + '\''
        + ", amount="
        + amount
        + ", currency="
        + currency
        + ", status='"
        + status
        + '\''
        + ", items="
        + items
        +
        // ", createdDate=" + createdDate +
        '}';
  }
}
