package com.example.myapplication;

// Another file where unit testing is done; it is part of the whole mocking process

public class SuggestedRoutesService {
    private SuggestedRoutes sRoutes;

    public SuggestedRoutesService(SuggestedRoutes sRoutes){
        this.sRoutes = sRoutes;
    }

    public String calcDist(String jeep_code, String location1, String location2){
        return sRoutes.calc_distance(jeep_code, location1, location2);
    }
}
