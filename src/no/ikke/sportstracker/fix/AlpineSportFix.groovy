package no.ikke.sportstracker.fix

import no.ikke.sportstracker.util.GPS

class AlpineSportFix {
    
    private def fileContent
    
    

    void fixFile (String filename) {
        fileContent = new File (filename).readLines ()
        fixContent()
    }
        
    void fixContent () {
        def lastlat
        def lastlng
        for (line in fileContent) {
            if (line.startsWith ("Time")) {
                // First line
                System.out.println(line)
            }
            else if (line.startsWith("#")) {
                System.out.println(line)
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
                        calcdist = GPS.haversin(lat, lng, lastlat, lastlng)
                        // System.out.println("Distance: "+haversin(lat, lng, lastlat, lastlng));
                        System.out.println(cols[0]+","+calcdist+","+cols[2]+","+cols[3]+","+cols[4]+","+cols[5]+","+cols[6]+","+cols[7])
                    } 
                    else {
                        System.out.println(line)
                    }
                    /*
                    if (dist > calcdist * 10) {
                        System.out.println(dist +"!="+calcdist)
                        System.out.println(line)
                    }
                    */
                    
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