package de.otaris.zertapps.privacychecker.database.dataSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import de.otaris.zertapps.privacychecker.database.DatabaseHelper;
import de.otaris.zertapps.privacychecker.database.model.Permission;

/**
 * Handles requests concerning Permissions to the database.
 * 
 */
public class PermissionDataSource extends DataSource<Permission> {

	private String[] allColumns = { Permission.ID, Permission.NAME,
			Permission.LABEL, Permission.DESCRIPTION, Permission.CRITICALITY };

	public PermissionDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	/**
	 * convert database result cursor (pointing to a result set) to app object
	 * 
	 * @param cursor
	 * @return cursor data as App object
	 */
	protected Permission cursorToModel(Cursor cursor) {

		if (cursor.getCount() == 0)
			return null;

		Permission permission = new Permission();

		permission.setId(cursor.getInt(0));
		permission.setName(cursor.getString(1));
		permission.setLabel(cursor.getString(2));
		permission.setDescription(cursor.getString(3));
		permission.setCriticality(cursor.getInt(4));

		return permission;
	}

	/**
	 * create app in DB by all attributes
	 * 
	 * @param name
	 * @param label
	 * @param version
	 * @param installed
	 * @param rating
	 * @return app object of the newly created app
	 */
	public Permission createPermission(String name, String label,
			String description, int criticality) {
		// set values for columns
		ContentValues values = new ContentValues();
		values.put(Permission.NAME, name);
		values.put(Permission.LABEL, label);
		values.put(Permission.DESCRIPTION, description);
		values.put(Permission.CRITICALITY, criticality);

		// insert into DB
		long insertId = database.insert(Permission.TABLE, null, values);

		// get recently inserted Permission by ID
		return getPermissionById(insertId);
	}
	/**
	 * Method to create a Permission only by it's name
	 * @param name
	 * 			-> String: Name of the permission 
	 * @return
	 * 			a Permission with all attributes set correctly
	 */

	public Permission createPermissionByName(String name) {
		// set values for columns
		ContentValues values = new ContentValues();
		values.put(Permission.NAME, name);
		values.put(Permission.LABEL, name);
		values.put(Permission.DESCRIPTION, name);
		values.put(Permission.CRITICALITY, 50);

		// insert into DB
		long insertId = database.insert(Permission.TABLE, null, values);

		// get recently inserted Permission by ID
		return getPermissionById(insertId);
	}

	/**
	 * gets single app by id from database
	 * 
	 * @param appId
	 *            id to identify a single app
	 * @return app object for given id
	 */
	public Permission getPermissionById(long permissionId) {
		// build database query
		Cursor cursor = database.query(Permission.TABLE, allColumns,
				Permission.ID + " = " + permissionId, null, null, null, null);
		cursor.moveToFirst();

		// convert to Permission object
		Permission newPermission = cursorToModel(cursor);
		cursor.close();

		// return Permission object
		return newPermission;
	}

	public Permission getPermissionByName(String permissionWithoutClassName) {
		Cursor cursor = database.query(Permission.TABLE, allColumns,
				Permission.NAME + " = '" + permissionWithoutClassName + "'",
				null, null, null, null);
		cursor.moveToFirst();

		Permission permission = cursorToModel(cursor);
		cursor.close();
		return permission;
	}
	/**
	 * Method to set all attributes of a permission by a given name
	 * @param name name of the permission 
	 * @return
	 * 		ContentValue with all attributes set correct 
	 */

	public ContentValues setContentValuesByName(String name) {

		String label;
		String description;
		int criticality;

		/*
		 * The following switch is ordered by criticality. The first permission
		 * is the most critical permission, hence it receives the max number of
		 * different permissions.
		 * 
		 * There are 146 different permissions.
		 */
		// int n = 146;

		switch (name) {
		case "SEND_SMS":
			label = "SMS senden";
			description = "App kann SMS versenden (ggf. im Hintergrund). Dies kann Kosten verursachen.";
			criticality = 1;

			return putValues(name, label, description, criticality);

		case "RECEIVE_SMS":
			label = "SMS empfangen";
			description = "App kann SMS empfangen und verarbeiten. App kann an Ger�t gesendete SMS �berwachen und l�schen (im Hintergrund).";
			criticality = 2;

			return putValues(name, label, description, criticality);

		case "RECEIVE_MMS":
			label = "MMS empfangen";
			description = "App kann MMS empfangen und verarbeiten. App kann an Ger�t gesendete MMS �berwachen und l�schen (im Hintergrund).";
			criticality = 3;

			return putValues(name, label, description, criticality);

		case "READ_CELL_BROADCAST":
			label = "Cell Broadcast-Nachrichten lesen";
			description = "App kann empfangene Cell Broadcast-Nachrichten lesen.";
			criticality = 4;

			return putValues(name, label, description, criticality);

		case "READ_SMS":
			label = "SMS oder MMS lesen";
			description = "App kann auf dem Telefon oder der SIM-Karte gespeicherte SMS bzw. MMS lesen.";
			criticality = 5;

			return putValues(name, label, description, criticality);

		case "WRITE_SMS":
			label = "SMS oder MMS bearbeiten";
			description = "App kann auf dem Telefon oder der SIM-Karte gespeicherte SMS bzw. MMS bearbeiten. App kann auch Nachrichten l�schen.";
			criticality = 6;

			return putValues(name, label, description, criticality);

		case "RECEIVE_WAP_PUSH":
			label = "Textnachrichten (WAP) empfangen";
			description = "App kann WAP-Nachrichten empfangen und verarbeiten. Nachrichten k�nnen �berwacht und gel�scht werden (im Hintergrund).";
			criticality = 7;

			return putValues(name, label, description, criticality);

		case "READ_CONTACTS":
			label = "Kontakte lesen";
			description = "App kann gespeicherte Kontakte lesen (einschlie�lich der H�ufigkeit, wie oft mit einer Person kommuniziert wurde). App kann diese Daten speichern und ggf. im Hintergrund weiterleiten.";
			criticality = 8;

			return putValues(name, label, description, criticality);

		case "WRITE_CONTACTS":
			label = "Kontakte �ndern";
			description = "App kann gespeicherte Kontaktdaten �ndern. App kann gespeicherte Kontaktdaten l�schen.";
			criticality = 9;

			return putValues(name, label, description, criticality);

		case "READ_CALL_LOG":
			label = "Anrufliste lesen";
			description = "App kann Anrufliste lesen (einschlie�lich Daten �ber ein-/ausgehende Anrufe). App kann diese Daten speichern und ggf. im Hintergrund weiterleiten.";
			criticality = 10;

			return putValues(name, label, description, criticality);

		case "WRITE_CALL_LOG":
			label = "Anrufliste bearbeiten";
			description = "App kann Anrufliste �ndern. App kann Anrufliste l�schen.";
			criticality = 11;

			return putValues(name, label, description, criticality);

		case "READ_SOCIAL_STREAM":
			label = "In sozialem Stream lesen";
			description = "App kann auf Updates aus sozialen Netzwerken zugreifen und diese synchronisieren.";
			criticality = 12;

			return putValues(name, label, description, criticality);

		case "WRITE_SOCIAL_STREAM":
			label = "In sozialem Stream schreiben";
			description = "App kann Updates aus sozialen Netzwerken von Ihren Freunden einblenden. App kann Nachrichten erstellen, die so erscheinen als stammten sie von einem Freund.";
			criticality = 13;

			return putValues(name, label, description, criticality);

		case "READ_PROFILE":
			label = "Meine Kontaktkarte lesen";
			description = "App kann auf dem Ger�t gespeicherte personenbezogene Profildaten (des Besitzers) lesen, einschlie�lich Ihres Namens und Ihrer Kontaktdaten. App kann Sie somit identifizieren und Ihre Profildaten anandere senden.";
			criticality = 14;

			return putValues(name, label, description, criticality);

		case "WRITE_PROFILE":
			label = "Meine Kontaktkarte �ndern";
			description = "App kann auf dem Ger�t gespeicherte personenbezogene Profildaten (des Besitzers) �ndern, einschlie�lich Ihres Namens und Ihrer Kontaktdaten, sowie Daten hinzuf�gen. App kann Sie somit identifizieren und Ihre Profildaten an andere senden.";
			criticality = 15;

			return putValues(name, label, description, criticality);

		case "READ_CALENDAR":
			label = "Kalendertermine lesen";
			description = "App kann auf dem Ger�t gespeicherte Termine lesen. App kann m�glicherweise Kalenderdaten weiterleiten oder speichern.";
			criticality = 16;

			return putValues(name, label, description, criticality);

		case "WRITE_CALENDAR":
			label = "Kalendertermine hinzuf�gen oder �ndern";
			description = "App kann auf dem Ger�t gespeicherte Termine �ndern, hinzuf�gen oder entfernen (im Hintergrund). App kann E-Mails an Terming�ste senden.";
			criticality = 17;

			return putValues(name, label, description, criticality);

		case "READ_USER_DICTIONARY":
			label = "Begriffe lesen, die zum W�rterbuch hinzugef�gt wurden";
			description = "App kann alle W�rter, Namen und Ausdr�cke lesen, die ein Nutzer in seinem W�rterbuch gespeichert hat.";
			criticality = 18;

			return putValues(name, label, description, criticality);

		case "READ_HISTORY_BOOKMARKS":
			label = "Lesezeichen f�r Webseiten und das Webprotokoll lesen";
			description = "App kann Verlauf aller mit dem Browser besuchten Adressen und s�mtliche Lesezeichen des Browsers lesen.";
			criticality = 19;

			return putValues(name, label, description, criticality);

		case "WRITE_HISTORY_BOOKMARKS":
			label = "Lesezeichen f�r Webseiten setzen und das Webprotokoll aufzeichnen";
			description = "App kann Browserverlauf und Lesezeichen �ndern und l�schen.";
			criticality = 20;

			return putValues(name, label, description, criticality);

		case "ADD_VOICEMAIL":
			label = "Mailbox-Nachrichten hinzuf�gen";
			description = "App kann Sprachnachrichten zum Mailbox-Posteingang hinzuf�gen.";
			criticality = 21;

			return putValues(name, label, description, criticality);

		case "ACCESS_FINE_LOCATION":
			label = "Genauer Standort (GPS- und netzwerkbasiert)";
			description = "App kann genaue Position anhand von GPS-Daten oder �ber Sendemasten/WLAN ermitteln.";
			criticality = 22;

			return putValues(name, label, description, criticality);

		case "ACCESS_COARSE_LOCATION":
			label = "Ungef�hrer Standort (netzwerkbasiert)";
			description = "App kann ungef�hren Standort �ber Sendemasten/WLAN ermitteln.";
			criticality = 23;

			return putValues(name, label, description, criticality);

		case "ACCESS_MOCK_LOCATION":
			label = "Simulierte Standortquellen f�r Testzwecke";
			description = "App kann simulierte Standortquellen f�r Testzwecke erstellen oder einen neuen Standortanbieter installieren. App kann GPS-Standort/-Status �berschreiben.";
			criticality = 24;

			return putValues(name, label, description, criticality);

		case "INTERNET":
			label = "Voller Netzwerkzugriff";
			description = "App kann eigene Internetverbindungen �ber benutzerdefinierte Netzwerkprotokolle herstellen. Der Browser und andere Apps bieten die M�glichkeit, Daten �ber das Internet zu versenden. Daher ist diese Berechtigung nicht zwingend erforderlich, um Daten �ber das Internet versenden zu k�nnen.";
			criticality = 25;

			return putValues(name, label, description, criticality);

		case "CHANGE_WIFI_STATE":
			label = "WLAN-Verbindungen herstellen und trennen";
			description = "App kann Verbindung zu WLAN-Zugriffpunkten herstellen und trennen, sowie �nderungen an den WLAN-Einstellungen vornehmen.";
			criticality = 26;

			return putValues(name, label, description, criticality);

		case "CHANGE_WIMAX_STATE":
			label = "WiMAX-Status �ndern";
			description = "App kann Verbindung zu WiMAX*-Netzwerken herstellen und trennen. WiMAX ist eine drahtlose Zugriffstechnik zu Breitbandinternet.";
			criticality = 27;

			return putValues(name, label, description, criticality);

		case "BLUETOOTH":
			label = "Pairing mit Bluetooth-Ger�ten durchf�hren";
			description = "App kann die Bluetooth-Konfiguration des Ger�tes einsehen und Verbindungen mit gekoppelten Ger�ten herstellen und akzeptieren.";
			criticality = 28;

			return putValues(name, label, description, criticality);

		case "BLUETOOTH_ADMIN":
			label = "Auf Bluetooth-Einstellungen zugreifen";
			description = "App kann Bluetooth-Einstellungen konfigurieren, Remote-Ger�te erkennen und Verbindungen zu diesen herstellen.";
			criticality = 29;

			return putValues(name, label, description, criticality);

		case "NFC":
			label = "Nahfeldkommunikation (NFC) steuern";
			description = "App kann mit NFC-Tags, Karten und Readern kommunizieren.";
			criticality = 30;

			return putValues(name, label, description, criticality);

		case "AUTHENTICATE_ACCOUNTS":
			label = "Konten erstellen und Passw�rter festlegen";
			description = "App kann die Kontoauthentifizierungsfunktion des Konto-Managers verwenden, einschlie�lich Erstellen von Konten und Abrufen und Festlegen der entsprechenden Passw�rter. Bspw. Google-, Twitter- oder Facebook-Konto.";
			criticality = 31;

			return putValues(name, label, description, criticality);

		case "USE_CREDENTIALS":
			label = "Konten auf dem Ger�t verwenden";
			description = "App kann sich �ber die Konten auf dem Ger�t anmelden (Authentifizierungstoken anfordern). App kann sich somit bspw. mit dem Google-Konto anmelden. Bspw. Google-, Twitter oder Facebook-Konto.";
			criticality = 32;

			return putValues(name, label, description, criticality);

		case "MANAGE_ACCOUNTS":
			label = "Konten hinzuf�gen oder entfernen";
			description = "App kann Konten dem Konto-Manager des Ger�ts hinzuf�gen oder Konten (inkl. deren Passw�rter) vom Ger�t l�schen. Bspw. Google-, Twitter oder Facebook-Konto.";
			criticality = 33;

			return putValues(name, label, description, criticality);

		case "CHANGE_WIFI_MULTICAST_STATE":
			label = "WLAN-Multicast-Empfang zulassen";
			description = "App kann Datenpakete empfangen, die mithilfe von Multicast-Adressen an s�mtliche Ger�te in einem WLAN-Netzwerk versendet wurden, nicht nur an Ihr Ger�t.";
			criticality = 34;

			return putValues(name, label, description, criticality);

		case "RECORD_AUDIO":
			label = "Audio aufnehmen";
			description = "App kann Ton mithilfe des Mikrofons aufnehmen (jederzeit, auch im Hintergrund).";
			criticality = 35;

			return putValues(name, label, description, criticality);

		case "CAMERA":
			label = "Bilder und Videos aufnehmen";
			description = "App kann Bilder und Videos mit der Kamera aufnehmen (jederzeit und ohne Ihre Best�tigung).";
			criticality = 36;

			return putValues(name, label, description, criticality);

		case "PROCESS_OUTGOING_CALLS":
			label = "Ausgehende Anrufe umleiten";
			description = "App kann ausgehende Anrufe verarbeiten und die zu w�hlende Nummer �ndern. App kann ausgehende Anrufe �berwachen, umleiten und unterbinden.";
			criticality = 37;

			return putValues(name, label, description, criticality);

		case "READ_PHONE_STATE":
			label = "Telefonstatus und Identit�t abrufen";
			description = "App kann Telefonnummer und Ger�te-IDs erfassen. App kann feststellen, ob gerade ein Gespr�ch gef�hrt wird und die Rufnummer verbundener Anrufer lesen.";
			criticality = 38;

			return putValues(name, label, description, criticality);

		case "CALL_PHONE":
			label = "Telefonnummern direkt anrufen";
			description = "App kann ohne Ihr Eingreifen Telefonnummern w�hlen (kein Notruf). Dies kann zu unerwarteten Kosten und Anrufen f�hren.";
			criticality = 39;

			return putValues(name, label, description, criticality);

		case "USE_SIP":
			label = "Internetanrufe t�tigen/annehmen";
			description = "App kann SIP-Dienste zum T�tigen und Annehmen von Internetanrufen verwenden.";
			criticality = 40;

			return putValues(name, label, description, criticality);

		case "WRITE_EXTERNAL_STORAGE":
			label = "SD-Karteninhalte �ndern oder l�schen";
			description = "App kann auf die SD-Karte schreiben und Daten darauf l�schen.";
			criticality = 41;

			return putValues(name, label, description, criticality);

		case "DISABLE_KEYGUARD":
			label = "Display-Sperre deaktivieren";
			description = "App kann Tastensperre (inkl. Passwortschutz) deaktivieren. Das Telefon deaktiviert die Sperre bspw. wenn ein Anruf eingeht und aktiviert sie nach dem Gespr�ch wieder.";
			criticality = 42;

			return putValues(name, label, description, criticality);

		case "GET_TASKS":
			label = "Aktive Apps abrufen";
			description = "App kann Infos zu aktuellen und k�rzlich ausgef�hrten Aufgaben abrufen. App kann damit m�glicherweise ermitteln, welche Apps auf dem Ger�t zum Einsatz kommen.";
			criticality = 43;

			return putValues(name, label, description, criticality);

		case "SYSTEM_ALERT_WINDOW":
			label = "�ber andere Apps einblenden";
			description = "App kann �ber andere Apps oder Teile der Benutzeroberfl�che Einblendungen vornehmen. Dies kann zu unerwarteten ver�nderten Darstellungen anderer Apps f�hren.";
			criticality = 44;

			return putValues(name, label, description, criticality);

		case "INSTALL_SHORTCUT":
			label = "Verkn�pfungen installieren";
			description = "App kann Verkn�pfungen zum Startbildschirm hinzuf�gen (ohne Eingreifen des Nutzers).";
			criticality = 45;

			return putValues(name, label, description, criticality);

		case "UNINSTALL_SHORTCUT":
			label = "Verkn�pfungen deinstallieren";
			description = "App kann Verkn�pfungen vom Startbildschirm entfernen (ohne Eingreifen des Nutzers).";
			criticality = 46;

			return putValues(name, label, description, criticality);

		case "SUBSCRIBED_FEEDS_WRITE":
			label = "Abonnierte Feeds schreiben";
			description = "App kann �nderungen an k�rzlich synchronisierten Newsfeeds vornehmen. App kann somit Ihre synchronisierten Newsfeeds �ndern. Newsfeeds sind �ber das Internet angebotene Nachrichtenstr�me, die mit einem Feedreader gelesen werden k�nnen. Damit lassen sich Webseiten, deren Inhalt sich h�ufig �ndert, verfolgen, ohne die Seite unmittelbar besuchen zu m�ssen.";
			criticality = 47;

			return putValues(name, label, description, criticality);

		case "CLEAR_APP_CACHE":
			label = "Alle Cache-Daten der App l�schen";
			description = "App kann Speicherplatz durch L�schen von Dateien in den Cache-Verzeichnissen anderer Apps freisetzen. Cache bezeichnet einen schnellen Zwischenspeicher, der (erneute) Zugriffe auf ein langsames Hintergrundmedium oder aufw�ndige Neuberechnungen zu vermeiden hilft.";
			criticality = 48;

			return putValues(name, label, description, criticality);

		default:
			label = "Selbsterstellte Berechtigung: " + name;
			description = "App fordert eine Berechtigung, die vom Entwickler selbst erstellt wurde. Die Funktionsweise ist unbekannt.";
			criticality = 1337;

			return putValues(name, label, description, criticality);

		}
	}

	/**
	 * Method to set given values for a Permission 
	 * @param name
	 * 			-> String: correct name of the permission 
	 * @param label
	 * 			-> String: German name of the permission 
	 * @param description
	 * 			-> String: Description for a permission 
	 * @param criticality
	 * 			-> int: Value for the criticality (1-> most critical)
	 * @return a ContentValue set with the correct parameters
	 *  
	 */
	private ContentValues putValues(String name, String label,
			String description, int criticality) {

		ContentValues values = new ContentValues();

		values.put(Permission.NAME, name);
		values.put(Permission.LABEL, label);
		values.put(Permission.DESCRIPTION, description);
		values.put(Permission.CRITICALITY, criticality);

		return values;

	}
}
