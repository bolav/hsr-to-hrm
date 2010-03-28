package no.ikke.sportstracker.util

class GPS {
    static float haversin (double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; //km
        
        double dlon = Math.toRadians(lon2 - lon1);
        double dlat = Math.toRadians(lat2 - lat1);
        
        double a = (Math.sin(dlat / 2)) * (Math.sin(dlat / 2)) + (Math.cos(lat1) * Math.cos(lat2) * (Math.sin(dlon / 2))) * (Math.cos(lat1) * Math.cos(lat2) * (Math.sin(dlon / 2)));
        
        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        double km = earthRadius * c;
        
        return (float) (km * 1000);
        
    }
    
}