package tdt4145;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Meny {
	static ResultSet result;
	static String query;
	public static void main(String[] args) throws SQLException {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("\n"
					+ "###########################\n"
					+ "Vil du:\n"
					+ "1) Registere apparat\n"
					+ "2) Registere �velse\n"
					+ "3) Registrere trenings�kt\n"
					+ "4) Sj� n siste trenings�kter\n"
					+ "5) Se resultatlogg i tidsintervall\n"
					+ "6) Lage �velsesgruppe\n"
					+ "7) Finne �velser i samme gruppe\n"
					+ "8) Finne �velser til apparat\n"
					+ "9) Printe en tabell\n"
					+ "0) Avslutt\n"
					+ "##############################\n");
			int svar = Integer.parseInt(scanner.nextLine());
			if(svar == 0) {
				break;
				
			}else if(svar == 1) {
				System.out.println("Hva er apparatets navn og beskrivelse?");
				String navn = scanner.nextLine();
				String beskrivelse = scanner.nextLine();
				query = "INSERT INTO apparat values(\""+navn+"\",\""+beskrivelse+"\")";
				Driver.Write(query);
				
			}else if(svar == 2){ //Usikker
				System.out.println("Navn *enter* Med eller uten apparat?");
				String navn =scanner.nextLine();
				String medUten = scanner.nextLine();
				if(medUten=="med") {
					System.out.println("Navn p� �velse *enter*, og (apparat komma kilo komma sett) adskilt av *enter*");
					while(scanner.hasNextLine()) {
						String[] input = scanner.nextLine().split(",");
						query = "insert into �velsemedapparat \""+ navn+"\",\""+input[0]+"\","+Integer.parseInt(input[1])+","+Integer.parseInt(input[2]);
					}
				
				}
				else if(medUten=="uten") {
					query = "INSERT INTO �velse values(\""+navn+"\")";
				}
				Driver.Write(query);
				
			}else if(svar == 3) {
				System.out.println("dato(����MMDD), tidspunkt(tt:mm:ss), varighet(tt:mm:ss), personligForm, prestasjon, notat?");
				String dato = scanner.nextLine();
				String tidspunkt = scanner.nextLine();
				String varighet = scanner.nextLine();
				int form = Integer.parseInt(scanner.nextLine());
				int prestasjon = Integer.parseInt(scanner.nextLine());
				String notat = scanner.nextLine();
				query = "INSERT INTO Trenings�kt (dato,tidspunkt,varighet,personligForm,prestasjon,notat)"
						+ " values (\""+dato+"\",\""+tidspunkt+"\",\""+varighet+"\","+form+","+prestasjon+",\""+notat+"\")";	
				Driver.Write(query); 
				
			}else if(svar == 4) { //Funker ikke
				System.out.println("skriv inn en n");
				int n = Integer.parseInt(scanner.nextLine());
				result = Driver.Read("SELECT * FROM trenings�kt ORDER BY dato DESC,tidspunkt DESC LIMIT "+ n+")");
				Driver.PrintSet(result);
			}
			else if(svar == 5) { //Usikker
				System.out.println("Start: tt:mm:ss,����MMDD *enter*\n"
						+ "Slutt: tt:mm:ss,��MMDD");
				String[] start = scanner.nextLine().split(",");
				String[] slutt = scanner.nextLine().split(",");
				
				query = "SELECT * \n" 
						+"FROM �velse NATURAL JOIN �velserForTrenings�kt NATURAL JOIN Trenings�kt \n"
						+ "where (trenings�kt.dato>=datostart(\""+start[1]+"\") \n"
						+ "and trenings�kt.dato<=datoslutt(\""+slutt[1]+"\") \n"
						+ "and ((trenings�kt.tidspunkt>=tidstart(\""+start[0]+"\") \n"
						+ "and trenings�kt.tidspunkt<=tidslutt(\""+slutt[0]+"\")) or (AddTime(trenings�kt.tidspunkt,trenings�kt.varighet)>=tidsstart \n"
						+ "and AddTime(trenings�kt.tidspunkt,trenings�kt.varighet)<=tidslutt) or (trenings�kt.tidspunkt<=tidsstart \n"
						+ "and AddTime(trenings�kt.tidspunkt,trenings�kt.varighet)>=tidslutt)));";
				result = Driver.Read(query);
				Driver.PrintSet(result);
				
			}else if(svar == 6) { //Usikker
				ArrayList<String> �velseliste = new ArrayList<>();
				System.out.println("Hvilken muskelgruppe?");
				String muskelgruppeNavn = scanner.nextLine();
				Driver.PrintTable("�velse");
				while(true) {
					System.out.println("Skriv inn �nsket �velse fra listen, \"quit\" for � hoppe ut av loop");
					String ny�velse = scanner.nextLine();
					if (ny�velse == "quit") {
						break;
					}
					else {
						�velseliste.add(ny�velse);
					}
				}
				for (String string : �velseliste) {
					query = "insert into \""+muskelgruppeNavn+"\" values(";
				}
			}
			else if(svar == 7) { //Usikker
				Driver.PrintTable("�velsesgruppe");
				System.out.println("Hvilken gruppe vil du se �velser fra?");
				String gruppe = scanner.nextLine();
				query = "SELECT �velsesnavn FROM �velsesgruppe NATURAL JOIN GruppeFor�velse\n" 
						+"where GruppeFor�velse.gruppenavn=gruppenavn(\""+gruppe+"\")";
				result = Driver.Read(query);
				Driver.PrintSet(result);
			}
			else if(svar == 8) { //Usikker
				System.out.println("Skriv navn p� apparat du vil se �velser til: ");
				String apparat = scanner.nextLine();
				query = "SELECT *\n"
						+"FROM �velse NATURAL JOIN �velseMedApparat NATURAL JOIN Apparat"
						+"where Apparat.navn=navn(\""+apparat+"\")";
				result = Driver.Read(query);
				Driver.PrintSet(result);
			}
			else if(svar == 9) {
				System.out.println("Hva vil du printe?");
				String skalPrintes = scanner.nextLine();
				Driver.PrintTable(skalPrintes);
				result = Driver.Read("select * from \""+skalPrintes+"\"");
				Driver.PrintSet(result);
			}
			else{
				System.out.println("Illegal input: try again");
			}
		}
	}
}
