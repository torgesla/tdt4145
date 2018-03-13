package tdt4145;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;
import java.sql.*;

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
				
			}else if(svar == 2){
				System.out.println("Med eller uten apparat?");
				String medUten = scanner.nextLine();
				if(medUten=="med") {
					System.out.println("Navn p� �velse *enter*, og (apparat komma kilo komma sett) adskilt av *enter*");
					String navn =scanner.nextLine();
					while(scanner.hasNextLine()) {
						String[] input = scanner.nextLine().split(",");
						query = "insert into �velsemedapparat \""+ navn+"\",\""+input[0]+"\","+Integer.parseInt(input[1])+","+Integer.parseInt(input[2]);
						System.out.println(query);
					}
				
				}
				else if(medUten=="uten") {
					
				}
				//System.out.println("Hva er �velsens navn?");
				//String navn = scanner.nextLine();
				//query = "INSERT INTO �velse values(\""+navn+"\")";
				//Driver.Write(query);
				
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
			else if(svar == 5) { //funker ikke
				query = "SELECT * \n" 
						+"FROM �velse NATURAL JOIN �velserForTrenings�kt NATURAL JOIN Trenings�kt \n"
						+ "where (trenings�kt.dato>=datostart('20131215') \n"
						+ "and trenings�kt.dato<=datoslutt('20131215') \n"
						+ "and ((trenings�kt.tidspunkt>=tidstart('07:00:00') \n"
						+ "and trenings�kt.tidspunkt<=tidslutt('18:00:00')) or (AddTime(trenings�kt.tidspunkt,trenings�kt.varighet)>=tidsstart \n"
						+ "and AddTime(trenings�kt.tidspunkt,trenings�kt.varighet)<=tidslutt) or (trenings�kt.tidspunkt<=tidsstart \n"
						+ "and AddTime(trenings�kt.tidspunkt,trenings�kt.varighet)>=tidslutt)));";
				result = Driver.Read(query);
				Driver.PrintSet(result);
				
			}else if(svar == 6) {
				ArrayList<String> �velseliste = new ArrayList<>();
				System.out.println("Hvilken muskelgruppe?");
				String muskelgruppeNavn = scanner.nextLine();
				Driver.PrintTable("�velse");
				while(true) {
					System.out.println("Skriv inn �nsket �velse fra listen, \"quit\" for � hoppe ut av loop");
					String ny�velse = scanner.nextLine();
					if (ny�velse != "quit") {
						�velseliste.add(ny�velse);
					}
					else {
						break;
					}
				}
				
			}
			else if(svar == 7) {
				
			}
			else if(svar == 8) {
				
			}
			else if(svar == 9) {
				System.out.println("Hva vil du printe?");
				String skalPrintes = scanner.nextLine();
				Driver.PrintTable(skalPrintes);
				result = Driver.Read("select * from \""+skalPrintes+"\"");
				Driver.PrintSet(result);
			}
			else if(svar == 10) {
				
			}
			else{
				System.out.println("Illegal input: try again");
			}
		}
	}
}
