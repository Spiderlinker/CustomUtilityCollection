package de.spiderlinker.database;

import de.spiderlinker.database.function.SqlConsumer;
import de.spiderlinker.database.function.SqlFunction;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionUtils {

	private DatabaseConnectionUtils() {
		// Utils-Klasse
	}

	/**
	 * Führt die gegebene Funktion aus und übergibt dieser eine Verbindung zur
	 * Datenbank
	 *
	 * @param function Funktion, die mit einer Datenbankverbindung ausgeführt werden
	 *                 soll
	 * @param <R>      Rückgabedatentyp der gegebenen Funktion
	 * @return Rückgabewert der gegebenen Function
	 * @throws SQLException Fehler bei der Ausführung
	 */
	private static <R> R runWithConnection(SqlFunction<Connection, R> function) throws SQLException {
		try (Connection conn = getConnection()) {
			return function.apply(conn);
		}
	}

	/**
	 * Führt den gegebenen Consumer aus und übergibt diesem eine Verbindung zur
	 * Datenbank
	 *
	 * @param consumer Consumer, der mit einer Datenbankverbindung ausgeführt werden
	 *                 soll
	 * @throws SQLException Fehler bei der Ausführung
	 */
	private static void runWithConnection(SqlConsumer<Connection> consumer) throws SQLException {
		try (Connection conn = getConnection()) {
			consumer.accept(conn);
		}
	}

	/**
	 * Führt die gegebene Funktion mit einer Datenbankverbindung aus. Bei der in die
	 * gegebene Funktion übergebene Connection ist AutoCommit deaktiviert. Der
	 * Commit erfolgt nach der Ausführung der Funktion. Falls während der Ausführung
	 * der Funktion etwas schief läuft, so wird ein Rollback durchgeführt. Am Ende
	 * wird die Connection geschlossen und der von der Funktion zurückgegebene Wert
	 * zurückgegeben.
	 *
	 * @param query Funktion, die mit einer Connection (mit ggf. Rollback bei
	 *              Fehlern) ausgeführt werden soll
	 * @param <R>   Rückgabedatentyp der gegebenen Funktion
	 * @return Rückgabewert der gegebenen Funktion
	 * @throws SQLException Fehler beim Verbinden zu der Datenbank
	 */
	private static <R> R runWithConnectionRollback(SqlFunction<Connection, R> query) throws SQLException {
		Connection conn = getConnection();
		R functionReturn = null;
		try {
			conn.setAutoCommit(false);
			functionReturn = query.apply(conn);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseConnectionUtils.close(conn);
		} finally {
			DatabaseConnectionUtils.rollback(conn);
		}
		return functionReturn;
	}

	/**
	 * Führt den gegebenen Consumer mit einer Datenbankverbindung aus. Bei der in den
	 * gegebenen Consumer übergebene Connection ist AutoCommit deaktiviert. Der
	 * Commit erfolgt nach der Ausführung des Consumers. Falls während der Ausführung
	 * des COnsumers etwas schief läuft, so wird ein Rollback durchgeführt. Am Ende
	 * wird die Connection geschlossen.
	 *
	 * @param query Consumer, der mit einer Connection (mit ggf. Rollback bei
	 *              Fehlern) ausgeführt werden soll
	 * @throws SQLException Fehler beim Verbinden zu der Datenbank
	 */
	private static void runWithConnectionRollback(SqlConsumer<Connection> query) throws SQLException {
		Connection conn = getConnection();
		try {
			conn.setAutoCommit(false);
			query.accept(conn);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseConnectionUtils.close(conn);
		} finally {
			DatabaseConnectionUtils.rollback(conn);
		}
	}

	/**
	 * Liefert eine Datenbankverbindung, auf der Operationen wie
	 * {@link Connection#prepareStatement} ausgeführt werden können
	 *
	 * @return Connection zur Datenbank
	 * @throws SQLException Fehler bei der Verbindung zur Datenbank
	 */
	private static Connection getConnection() throws SQLException {
		return DatabaseConnection.getConnection();
	}

	/**
	 * Schlie�t das gegebene Objekt des Types {@link AutoCloseable} und f�ngt die
	 * eventuell geworfene Exception ab (und gibt diese ggf. auf der Konsole aus)
	 * 
	 * @param c Object, das geschlossen werden soll
	 */
	public static void close(AutoCloseable c) {
		if (c != null) {
			try {
				c.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Ruft {@link Connection#rollback()} auf der gegebenen {@link Connection} auf
	 * und f�ngt die eventuell geworfene Exception ab (und gibt diese ggf. auf der
	 * Konsole aus)
	 * 
	 * @param conn Connection, auf der ein rollback ausgef�hrt werden soll
	 */
	public static void rollback(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
