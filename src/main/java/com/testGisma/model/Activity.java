package com.testGisma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "activities")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Activity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long id;
    
    @Column
    public String description;
    
    @Column
    public Date createdAt;
    
    @Column
    public Boolean current;
}
