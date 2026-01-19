package lvtPicerija;

import java.io.IOException;
import java.nio.file.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Storage {
    private static final Storage INSTANCE = new Storage();
    private final Path productsFile = Paths.get("products.txt");
    private final Path ordersFile = Paths.get("orders.txt");
    private final DateTimeFormatter fmt = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    
     
            
            