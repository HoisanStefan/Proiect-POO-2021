import com.fasterxml.jackson.databind.ObjectMapper;
import mappingclass.MappingClass;
import mappingclass.MappingClassOutput;
import handler.Handler;

import java.io.File;
import java.io.FileWriter;
/**
 * Entry point to the simulation
 */
public final class Main {

    private Main() { }

    /**
     * Main function which reads the input file and starts simulation
     *
     * @param args input and output files
     * @throws Exception might error when reading/writing/opening files, parsing JSON
     */
    public static void main(final String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        File in = new File(args[0]);
        FileWriter out = new FileWriter(args[1]);


        String file = args[0];
        String s1 = file.substring(0, 39);
        String s2 = file.substring(60);
        file = s1 + s2;
        FileWriter out2 = new FileWriter(file);
        //System.out.println(file);

        MappingClass mc = objectMapper.readValue(in, MappingClass.class);
        Handler h = new Handler(mc);
        MappingClassOutput mco = h.getResult();

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, mco);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(out2, mco);

    }
}
