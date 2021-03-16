package Excel;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Model.Persona;

public class Main {
	
	static Logger log = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException {
		
		
		System.out.println("######################## INICIO ########################");
		log.info("######################## INICIO ########################");

		ArrayList<String> listaTitulos = new ArrayList<String>();
		listaTitulos.add("Nombre");
		listaTitulos.add("Apellido");
		listaTitulos.add("Edad");
		listaTitulos.add("Oficio");

		Persona persona1 = new Persona("Esteban", "Bustos", 33, "Programador RPA");
		Persona persona2 = new Persona("Mirtha", "Castro", 32, "Consultor Informático");
		Persona persona3 = new Persona("Jose", "Collio", 33, "DBA");
		Persona persona4 = new Persona("Herman", "Vargas", 35, "Docente POO");
		ArrayList<Persona> listaPersona = new ArrayList<Persona>();
		listaPersona.add(persona1);
		listaPersona.add(persona2);
		listaPersona.add(persona3);
		listaPersona.add(persona4);

		//Excel
		Workbook workbook = new XSSFWorkbook();
		Sheet hoja = workbook.createSheet("Registro Personas");
		Row fila = hoja.createRow(0);

		for (int i = 0; i < listaTitulos.size(); i++) {
			Cell celda = fila.createCell(i);
			celda.setCellValue(listaTitulos.get(i));

			// Estilos
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			celda.setCellStyle(cellStyle);
		}

		fila = hoja.createRow(1);

		for (int i = 0; i < listaPersona.size(); i++) {

			Row filaDatos = hoja.createRow(i + 1);

			Cell celdaNombre = filaDatos.createCell(0);
			celdaNombre.setCellValue(listaPersona.get(i).getNombre());

			Cell celdaApellido = filaDatos.createCell(1);
			celdaApellido.setCellValue(listaPersona.get(i).getApellido());

			Cell celdaEdad = filaDatos.createCell(2);
			celdaEdad.setCellValue(listaPersona.get(i).getEdad());

			Cell celdaOficio = filaDatos.createCell(3);
			celdaOficio.setCellValue(listaPersona.get(i).getOficio());
			
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			celdaNombre.setCellStyle(cellStyle);
			celdaApellido.setCellStyle(cellStyle);
			celdaEdad.setCellStyle(cellStyle);
			celdaOficio.setCellStyle(cellStyle);

			hoja.autoSizeColumn(i);
		}

		try {
			File archivo = new File("reporte.xlsx");
			FileOutputStream salida = new FileOutputStream(archivo);
			workbook.write(salida);
			System.out.println("Archivo excel generado correctamente");
			log.info("Archivo excel generado correctamente");
		} catch (Exception e) {
			System.out.println("No se pudo generar archivo excel por : " + e.getMessage());
			log.info("No se pudo generar archivo excel por : " + e.getMessage());
		}

		// PDF
		try {

			//Objetos PDF
			PDDocument pdf = new PDDocument();
			PDPage pagina = new PDPage();
			pdf.addPage(pagina);
			
			//Propiedades del pdf
			PDDocumentInformation pdd = pdf.getDocumentInformation();
			pdd.setAuthor("Carlos Ortiz");
			pdd.setTitle("PDF - Registro Personas");
			pdd.setSubject("PDF - Registro Personas");
			Calendar date = new GregorianCalendar();
			date.set(2021, 11, 03);
			pdd.setCreationDate(date);
			
			//Contenido del PDF
			PDPageContentStream contentStream = new PDPageContentStream(pdf, pagina);
			contentStream.beginText();
			contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
			contentStream.newLineAtOffset(25, 750);
			
			contentStream.showText("Registro de personas: ");
			
			contentStream.setLeading(14.5f);
			contentStream.newLine();
			contentStream.newLine();
			
			for (int i = 0; i < listaPersona.size(); i++) {
				contentStream.showText("Nombre: " + listaPersona.get(i).getNombre());
				contentStream.newLine();
				contentStream.showText("Apellido: " + listaPersona.get(i).getApellido());
				contentStream.newLine();
				contentStream.showText("Edad: " + String.valueOf(listaPersona.get(i).getEdad()));
				contentStream.newLine();
				contentStream.showText("Oficio: " + listaPersona.get(i).getOficio());
				contentStream.newLine();
				contentStream.newLine();
			}
			
			contentStream.endText();
			
			//Imagenes
//			PDImageXObject pdImage = PDImageXObject.createFromFile("C:/img.png", pdf);
//          	contentStream.drawImage(pdImage, 0, 200);

			contentStream.close();
			pdf.save("reporte.pdf");
			pdf.close();
			
			System.out.println("Archivo PDF generado correctamente");
			log.info("Archivo PDF generado correctamente");

		} catch (Exception e) {
			System.out.println("No se pudo generar archivo pdf por : " + e.getMessage());
			log.info("No se pudo generar archivo pdf por : \" + e.getMessage()");
		}
		
		
		//MYSQL
		try {
			
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_springboot","root","sasa");
			
			//SELECT
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM  comunas");
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				System.out.println ("Comuna: " + resultSet.getString("nombre"));
				log.info("Comuna: " + resultSet.getString("nombre"));
			}
			
			//INSERT
			preparedStatement = connection.prepareStatement("INSERT INTO accesos VALUES (?,?,?)");
			preparedStatement.setString(1,null);
			preparedStatement.setString(2,"cubo_k@hotmail.com");
			preparedStatement.setString(3,"Ahola123");
			preparedStatement.executeUpdate();
			
			//UPDATE
			String correoNuevo ="insertCorreoNuevo@gmail.com";
			preparedStatement = connection.prepareStatement("UPDATE accesos set email='"+ correoNuevo + "' where email='cubo_k@hotmail.com' ");
			preparedStatement.executeUpdate();
			
			//TRUNCATE
//			preparedStatement = connection.prepareStatement("TRUNCATE TABLE accesos");
//			preparedStatement.executeUpdate();
			
			connection.close();
			
		} catch (Exception e) {
			System.out.println("Error de conexion con BDD :" + e.getMessage());
			log.info("Error de conexion con BDD :" + e.getMessage());
		}
		
		//C CREATE 		INSERT	
		//R READ		SELECT
		//U UPDATE		UPDATE (NO HAY)
		//D DELETE		DELETE O TRUNCATE
		

		System.out.println("######################## FIN ########################");
		log.info("######################## FIN ########################");

	}

}
