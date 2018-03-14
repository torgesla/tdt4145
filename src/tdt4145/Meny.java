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
					+ "2) Registere øvelse\n"
					+ "3) Registrere treningsøkt\n"
					+ "4) Sjå n siste treningsøkter\n"
					+ "5) Se resultatlogg i tidsintervall\n"
					+ "6) Lage øvelsesgruppe\n"
					+ "7) Finne øvelser i samme gruppe\n"
					+ "8) Finne øvelser til apparat\n"
					+ "9) Printe en tabell\n"
					+ "0) Avslutt\n"
					+ "##############################\n");
			int svar = Integer.parseInt(scanner.nextLine());
			if(svar == 0) {
				break;
				
			}else if(svar == 1) { //Funker
				System.out.println("Hva er apparatets navn og beskrivelse?");
				String navn = scanner.nextLine();
				String beskrivelse = scanner.nextLine();
				query = "INSERT INTO apparat values(\""+navn+"\",\""+beskrivelse+"\")";
				Driver.Write(query);
				
			}else if(svar == 2){
				System.out.println("Navn *enter* med eller uten apparat?");
				String navn =scanner.nextLine();
				System.out.println(navn);
				String medUten = scanner.nextLine();
				System.out.println(medUten);
				if(medUten.equals("med")) { //Funker ikke ForeginKey-ERROR
					System.out.println("(kilo komma sett komma apparatnavn) adskilt av *enter*");
					while(scanner.hasNextLine()) {
						String[] input = scanner.nextLine().split(",");
						query = "INSERT INTO øvelsemedapparat values( \""+ navn+"\","+Integer.parseInt(input[0])+","+Integer.parseInt(input[1])+",\""+input[2]+"\")";
						System.out.println(query);
						Driver.Write(query);
					}
				
				}
				else if(medUten.equals("uten")) { //Funker
					query = "INSERT INTO øvelse values(\""+navn+"\")";
					Driver.Write(query);
				}
				
				
			}else if(svar == 3) { //Funker
				System.out.println("dato(ÅÅÅÅMMDD), tidspunkt(tt:mm:ss), varighet(tt:mm:ss), personligForm, prestasjon, notat?");
				String dato = scanner.nextLine();
				String tidspunkt = scanner.nextLine();
				String varighet = scanner.nextLine();
				int form = Integer.parseInt(scanner.nextLine());
				int prestasjon = Integer.parseInt(scanner.nextLine());
				String notat = scanner.nextLine();
				query = "INSERT INTO Treningsøkt (dato,tidspunkt,varighet,personligForm,prestasjon,notat)"
						+ " values (\""+dato+"\",\""+tidspunkt+"\",\""+varighet+"\","+form+","+prestasjon+",\""+notat+"\")";	
				Driver.Write(query); 
				
			}else if(svar == 4) { //Funker
				System.out.println("skriv inn en n");
				int n = Integer.parseInt(scanner.nextLine());
				query = "SELECT * FROM treningsøkt ORDER BY dato DESC,tidspunkt DESC LIMIT "+ n;
				result = Driver.Read(query);
				Driver.PrintSet(result);
			}
			else if(svar == 5) { //Usikker
				System.out.println("Start: tt:mm:ss,ÅÅÅÅMMDD *enter*\n"
						+ "Slutt: tt:mm:ss,ÅÅMMDD");
				String[] start = scanner.nextLine().split(",");
				String[] slutt = scanner.nextLine().split(",");
				
				query = "SELECT * \n" 
						+"FROM Øvelse NATURAL JOIN ØvelserForTreningsøkt NATURAL JOIN Treningsøkt \n"
						+ "where (treningsøkt.dato>=datostart(\""+start[1]+"\") \n"
						+ "and treningsøkt.dato<=datoslutt(\""+slutt[1]+"\") \n"
						+ "and ((treningsøkt.tidspunkt>=tidstart(\""+start[0]+"\") \n"
						+ "and treningsøkt.tidspunkt<=tidslutt(\""+slutt[0]+"\")) or (AddTime(treningsøkt.tidspunkt,treningsøkt.varighet)>=tidsstart \n"
						+ "and AddTime(treningsøkt.tidspunkt,treningsøkt.varighet)<=tidslutt) or (treningsøkt.tidspunkt<=tidsstart \n"
						+ "and AddTime(treningsøkt.tidspunkt,treningsøkt.varighet)>=tidslutt)))";
				System.out.println(query);
				result = Driver.Read(query);
				Driver.PrintSet(result);
				
			}else if(svar == 6) { //Funker ikke ForeignKEY-ERROR
				ArrayList<String> øvelseliste = new ArrayList<>();
				System.out.println("Hvilken muskelgruppe?");
				String muskelgruppeNavn = scanner.nextLine();
				Driver.PrintTable("øvelse");
				while(true) {
					System.out.println("Skriv inn ønsket øvelse fra listen, \"quit\" for å hoppe ut av loop");
					String nyØvelse = scanner.nextLine();
					if (nyØvelse.equals("quit")) {
						break;
					}
					else {
						øvelseliste.add(nyØvelse);
					}
				}
				for (String øvelse : øvelseliste) {
					query = "insert into gruppeforøvelse values(\""+øvelse+"\",\""+muskelgruppeNavn+"\")";
					Driver.Write(query);
				}
			}
			else if(svar == 7) { //Usikker-Sjekke etter at func-6 er på plass
				Driver.PrintTable("øvelsesgruppe");
				System.out.println("Hvilken gruppe vil du se øvelser fra?");
				String gruppe = scanner.nextLine();
				query = "SELECT øvelsesnavn FROM Øvelsesgruppe NATURAL JOIN GruppeForØvelse\n" 
						+"where GruppeForØvelse.gruppenavn=gruppenavn(\""+gruppe+"\")";
				result = Driver.Read(query);
				Driver.PrintSet(result);
			}
			else if(svar == 8) { //Usikker ForeignKey-ERROR
				System.out.println("Skriv navn på apparat du vil se øvelser til: ");
				String apparat = scanner.nextLine();
				query = "SELECT *\n"
						+"FROM Øvelse NATURAL JOIN ØvelseMedApparat NATURAL JOIN Apparat"
						+" where Apparat.navn=navn(\""+apparat+"\")";
				System.out.println(query);
				result = Driver.Read(query);
				Driver.PrintSet(result);
			}
			else if(svar == 9) { //Funker
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
