import java.util.HashMap;
import java.util.Map;

public class Material {


    final private String name;
    final private double baseProductionTime;
    final private ProductionType productionType;
    final private Map<String,Double> preRequisites;

    public Material(String name, double baseProductionTime, ProductionType productionType, Map<String,Double> preRequisites){
        this.name = name;
        this.baseProductionTime = baseProductionTime;
        this.productionType = productionType;
        this.preRequisites = preRequisites;
    }

    public String getName(){
        return name;
    }

    public double getBaseProductionTime(){
        return baseProductionTime;
    }

    public ProductionType getProductionType() {return productionType;}

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


    public static Map<String,Double> calculateProductionInfrastructure(Material material, Double quantityPerSecond) {

        Map<String, Double> infrastructureMap = new HashMap<>();


        double NumberOfProductionFacilities = (material.getBaseProductionTime() * quantityPerSecond) / (GlobalVariables.productionFactorMap.get(material.getProductionType()));
        infrastructureMap.put(material.getName(), NumberOfProductionFacilities);

        for (var m : material.getPreRequisites().keySet()) {
            Map<String, Double> materialMapRecursive = calculateProductionInfrastructure(GlobalVariables.materialMap.get(m), quantityPerSecond * material.getPreRequisites().get(m));
            materialMapRecursive.forEach((key, value) ->
                    infrastructureMap.merge(key, value, Double::sum)
            );
        }

        return infrastructureMap;
    }






}
