import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public class FactorioCalculator {


    public static void main(String[] args){

        String requestedItem = "cliff explosives";



        double quantityRequested_perSecond = 0.1;

        double factorDriller = 0.6; //production per second needed
        double factorFurnance = 2.0;
        double factorWorkbench = 0.75;

        double factorChemicalPlant = 1.0;
        double factorOilRefinery = 1.0;
        double factorPump = 1.0;
        double factorPumpjack = 1.0;
        double factorMultiple = 1.0;
        double factorOther = 1.0;

        GlobalVariables.productionFactorMap.put(ProductionType.DRILLER,factorDriller);
        GlobalVariables.productionFactorMap.put(ProductionType.FURNANCE,factorFurnance);
        GlobalVariables.productionFactorMap.put(ProductionType.WORKBENCH,factorWorkbench);
        GlobalVariables.productionFactorMap.put(ProductionType.CHEMICALPLANT,factorChemicalPlant);
        GlobalVariables.productionFactorMap.put(ProductionType.OILREFINERY,factorOilRefinery);
        GlobalVariables.productionFactorMap.put(ProductionType.PUMP,factorPump);
        GlobalVariables.productionFactorMap.put(ProductionType.PUMPJACK,factorPumpjack);
        GlobalVariables.productionFactorMap.put(ProductionType.MULTIPLE,factorMultiple);
        GlobalVariables.productionFactorMap.put(ProductionType.OTHER,factorOther);


        Path MaterialList = Path.of("Ressources", "MaterialListAdvanced.txt");
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
                currentLine = currentLine.substring(indexStopSymbol+1);

                indexStopSymbol = currentLine.indexOf(";");
                ProductionType productionType = ProductionType.valueOf(currentLine.substring(0,indexStopSymbol));
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

                GlobalVariables.materialMap.put(materialName,new Material(materialName,materialBaseProductionTime,productionType,preRequisitesMap));

                currentLine = br.readLine();

            }


        } catch (IOException e) {e.printStackTrace();}


        System.out.println(Material.calculateRequiredRawMaterials(GlobalVariables.materialMap.get(requestedItem),quantityRequested_perSecond));

        System.out.println(Material.calculateProductionInfrastructure(GlobalVariables.materialMap.get(requestedItem),quantityRequested_perSecond));








    }
}
