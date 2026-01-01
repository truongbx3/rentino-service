package com.viettel.vss.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Repository
public class SequenceRepo {
    @PersistenceContext
    private EntityManager em;

    public long nextOrderSeq() {
        return ((Number) em
                .createNativeQuery("SELECT NEXT VALUE FOR order_seq")
                .getSingleResult()).longValue();
    }
    public String getSequenceNo(){
        String seq = "HD" + String.format("%08d", nextOrderSeq());
        return seq;
    }
}
