package com.example.myapplication;

// Another file where unit testing is done; it is part of the whole mocking process

public class FareService {
    private SuggestedRoutes sRoutes;

    public FareService(SuggestedRoutes sRoutes){
        this.sRoutes = sRoutes;
    }

    public float calcFare(float distance, float baseFare, float perKm, float firstKm){
        return sRoutes.calculateFare(distance, baseFare, perKm, firstKm);
    }
}
