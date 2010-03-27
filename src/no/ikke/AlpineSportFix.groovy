package no.ikke

class AlpineSportFix {
    
    private def fileContent
    
    

    void fixFile (String filename) {
        fileContent = new File (filename).readLines ()
        fixContent()
    }
    
    float haversin (double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; //km
        
        double dlon = Math.toRadians(lon2 - lon1);
        double dlat = Math.toRadians(lat2 - lat1);
        
        double a = (Math.sin(dlat / 2)) * (Math.sin(dlat / 2)) + (Math.cos(lat1) * Math.cos(lat2) * (Math.sin(dlon / 2))) * (Math.cos(lat1) * Math.cos(lat2) * (Math.sin(dlon / 2)));
        
        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        double km = earthRadius * c;
        
        return (float) (km * 1000);
        
    }
    
    void fixContent () {
        def lastlat
        def lastlng
        for (line in fileContent) {
            if (line.startsWith ("Time")) {
                // First line
            }
            else if (line.startsWith("#")) {
                
            }
            else {
            
                def cols = line.split(",")
                if (cols.length == 8) {
                    double dist = 0.0 
                    if (cols[1] != "") {
                        dist = cols[1].toDouble()
                    }
                    double lat = cols[5].toDouble()
                    double lng = cols[6].toDouble()
                    
                    double calcdist = 0;
                    if (lastlat != null) {
                        // System.out.println(lat+","+lng+" - "+lastlat+","+lastlng+" ("+ dist +")")
                        calcdist = haversin(lat, lng, lastlat, lastlng)
                        // System.out.println("Distance: "+haversin(lat, lng, lastlat, lastlng));
                    }
                    if (dist > calcdist * 10) {
                        System.out.println(dist +"!="+calcdist)
                        System.out.println(line)
                    }
                    
                    lastlat = lat
                    lastlng = lng
                }
            }
        }
    }
    
    
    public static void main (String[] args)  {
        AlpineSportFix app = new AlpineSportFix();
        app.fixFile(args[0]);
    }
    
}