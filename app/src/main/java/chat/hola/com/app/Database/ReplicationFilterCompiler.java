package chat.hola.com.app.Database;

/*
 * Created by moda on 09/01/17.
 */


/**
 * A delegate that can be invoked to compile source code into a ReplicationFilter.
 */
interface ReplicationFilterCompiler {
    /**
     * Compile Filter Function
     *
     * @param source   The source code to compile into a ReplicationFilter.
     * @param language The language of the source.
     * @return A compiled ReplicationFilter.
     */
    ReplicationFilter compileFilterFunction(String source, String language);
}
