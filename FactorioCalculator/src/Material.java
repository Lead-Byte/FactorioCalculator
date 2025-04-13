import java.util.HashMap;
import java.util.Map;

public class Material {


    private String name;
    private double baseProductionTime;
    private Map<String,Double> preRequisites;

    public Material(String name, double baseProductionTime, Map<String,Double> preRequisites){
        this.name = name;
        this.baseProductionTime = baseProductionTime;
        this.preRequisites = preRequisites;
    }

    public String getName(){
        return name;
    }

    public double getBaseProductionTime(){
        return baseProductionTime;
    }

    public Map<String,Double> getPreRequisites() {
        return preRequisites;
    }




    public static Map<String,Double> calculateRequiredRawMaterials(Material material, Double quantity) {

        Map<String,Double> materialMap = new HashMap<>();

        if (material.getPreRequisites().isEmpty()) {materialMap.put(material.getName(),quantity);}
        else {

            for (var m : material.getPreRequisites().keySet()) {


                Map<String, Double> materialMapRecursive = calculateRequiredRawMaterials(GlobalVariables.materialMap.get(m), quantity*material.getPreRequisites().get(m));
                materialMapRecursive.forEach((key, value) ->
                        materialMap.merge(key, value, Double::sum)
                );
            }
        }
        return materialMap;
    }


    public static Map<String,Double> calculateProductionInfrastructure(Material material, Double quantityPerSecond, Double productionRateWorkshops, Double productionRateHarvesters) {

        Map<String,Double> infrastructureMap = new HashMap<>();

        if (material.getPreRequisites().isEmpty()) {
            double NumberOfHarvesters = (material.getBaseProductionTime()*quantityPerSecond)/(productionRateHarvesters);
            infrastructureMap.put(material.getName(),NumberOfHarvesters);}
        else {

            double NumberOfWorkshops = (material.getBaseProductionTime()*quantityPerSecond)/(productionRateWorkshops);
            infrastructureMap.put(material.getName(),NumberOfWorkshops);

            for (var m : material.getPreRequisites().keySet()) {
                Map<String, Double> materialMapRecursive = calculateProductionInfrastructure(GlobalVariables.materialMap.get(m), quantityPerSecond*material.getPreRequisites().get(m),productionRateWorkshops,productionRateHarvesters);
                materialMapRecursive.forEach((key, value) ->
                        infrastructureMap.merge(key, value, Double::sum)
                );
            }
        }
        return infrastructureMap;
    }






}
