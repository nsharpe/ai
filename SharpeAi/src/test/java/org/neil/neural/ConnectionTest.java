package org.neil.neural;

import org.junit.Test;

public class ConnectionTest {

    @Test
    public void testSubtractBug(){
        Node source = new NodeDefault(1, 100, 10);
        source.addToStorage(10);

        Connection connection = new Connection(source,source, 100, 1, Connection.ConnectionType.SUBTRACT);
        connection.activate();
    }
}
