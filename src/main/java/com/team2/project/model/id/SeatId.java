package com.team2.project.model.id;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import lombok.Data;

@Data
public class SeatId implements Serializable {
    
    private int seatNo;
    private int showNo;
    private Date showDate;

    public SeatId() {}

    public SeatId(int seatNo, int showNo, Date showDate) {
        this.seatNo = seatNo;
        this.showNo = showNo;
        this.showDate = showDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatId seatId = (SeatId) o;
        return seatNo == seatId.seatNo &&
               showNo == seatId.showNo &&
               Objects.equals(showDate, seatId.showDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatNo, showNo, showDate);
    }
}
