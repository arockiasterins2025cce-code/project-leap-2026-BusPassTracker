package com.example.buspass.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "bus_routes")
public class BusRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String routeNumber;

    @NotBlank
    @Column(nullable = false)
    private String routeName;

    @NotBlank
    @Column(nullable = false)
    private String boardingPoint;

    public BusRoute() {}

    public BusRoute(String routeNumber, String routeName, String boardingPoint) {
        this.routeNumber = routeNumber;
        this.routeName = routeName;
        this.boardingPoint = boardingPoint;
    }

    public Long getId() { return id; }
    public String getRouteNumber() { return routeNumber; }
    public String getRouteName() { return routeName; }
    public String getBoardingPoint() { return boardingPoint; }

    public void setId(Long id) { this.id = id; }
    public void setRouteNumber(String routeNumber) { this.routeNumber = routeNumber; }
    public void setRouteName(String routeName) { this.routeName = routeName; }
    public void setBoardingPoint(String boardingPoint) { this.boardingPoint = boardingPoint; }
}
