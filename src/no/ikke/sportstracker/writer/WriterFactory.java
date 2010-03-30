package no.ikke.sportstracker.writer;

import de.saring.polarviewer.core.PVException;
import java.util.ServiceLoader;

public class WriterFactory {
        
        private static WriterFactory instance;
        ServiceLoader<WriterInterface> writerLoader;
        
        
        private WriterFactory () {
            writerLoader =  ServiceLoader.load(WriterInterface.class);
        }
        
        
        public static WriterInterface getWriter (String filename) throws PVException {        
            createInstance ();

            // return the parser implementation which matches the filename suffix
            for (WriterInterface writer : instance.writerLoader) {
                if (filename.endsWith("." + writer.getInfo().getSuffix())) {
                    return writer;
                }
            }

            throw new PVException ("No writer has been found for filename '" + filename + "' ...");
        }
        
        /**
         * Creates the singleton instance of the factory when not done yet.
         */
        private static synchronized void createInstance () {        
            if (instance == null) {
                instance = new WriterFactory();
            }
        }    
        
    
}