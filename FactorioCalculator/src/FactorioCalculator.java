import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public class FactorioCalculator {


    public static void main(String[] args){

        String requestedItem = "transport belt";

        double quantityRequested_perSecond = 10;
        double quantityRequested_perMinute = 60*quantityRequested_perSecond;


        Path MaterialList = Path.of("Ressources", "MaterialList.txt");
        String currentLine;
        int indexStopSymbol;



        try (BufferedReader br = Files.newBufferedReader(MaterialList)) {

            currentLine = br.readLine();
            while (currentLine!=null) {

                Map<String,Double> preRequisitesMap = new HashMap<>();

                indexStopSymbol = currentLine.indexOf(";");
                String materialName = currentLine.substring(0,indexStopSymbol);

                currentLine = currentLine.substring(indexStopSymbol+1);

                indexStopSymbol = currentLine.indexOf(";");
                double materialBaseProductionTime = Double.parseDouble(currentLine.substring(0,indexStopSymbol));
                currentLine = currentLine.substring(indexStopSymbol);

                while (currentLine.length() > 1) {

                    currentLine = currentLine.substring(1);
                    indexStopSymbol = currentLine.indexOf(";");

                    String preRequisiteName  = currentLine.substring(0,indexStopSymbol);
                    currentLine = currentLine.substring(indexStopSymbol+1);
                    indexStopSymbol = currentLine.indexOf(";");
                    Double preRequisiteCount = Double.parseDouble(currentLine.substring(0,indexStopSymbol));
                    currentLine = currentLine.substring(indexStopSymbol);

                    preRequisitesMap.put(preRequisiteName,preRequisiteCount);

                }

                GlobalVariables.materialMap.put(materialName,new Material(materialName,materialBaseProductionTime,preRequisitesMap));

                currentLine = br.readLine();

            }


        } catch (IOException e) {e.printStackTrace();}


        System.out.println(Material.calculateRequiredRawMaterials(GlobalVariables.materialMap.get(requestedItem),quantityRequested_perMinute));

        System.out.println(Material.calculateProductionInfrastructure(GlobalVariables.materialMap.get(requestedItem),quantityRequested_perSecond,2.0,0.6));








    }
}
