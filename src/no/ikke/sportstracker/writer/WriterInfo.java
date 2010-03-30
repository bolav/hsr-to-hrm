package no.ikke.sportstracker.writer;

public class WriterInfo {
    /** The name of the writer. */
    private String name;
    
    /** List of exercise file suffixes which can be read by this writer. */
    private String suffix;

    /**
     * Creates a new WriterInfo instance.
     * @param name 
     * @param suffixes list of exercise file suffixes which can be written by this parser
     */
    public WriterInfo (String name, String suffix) {
        this.name = name;
        this.suffix = suffix;
    }

    /***** BEGIN: Generated Getters and Setters *****/
    
    public String getName () {
        return name;
    }

    public String getSuffix () {
        return suffix;
    }

    /***** END: Generated Getters and Setters *****/
    
}