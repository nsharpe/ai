package org.neil.neural;

import org.junit.jupiter.api.Test;

public class ConnectionTest {

    @Test
    public void testSubtractBug(){
        Node source = new MutateableNodeDefault(1, 100, 10);
        source.addToStorage(10);

        Connection connection = new Connection(source,source, 100, 1, Connection.ConnectionType.SUBTRACT);
        connection.activate();
    }
}
