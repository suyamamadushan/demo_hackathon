import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SearchFood {

    enum keys {
        Sedentary,
        ModeratelyActive,
        Active
    }
    HashMap<Integer,HashMap<String,Integer>> child;
    HashMap<Integer,HashMap<String,Integer>> female;
    HashMap<Integer,HashMap<String,Integer>> male;

    List<List<String>> records;
    List<List<String>> matches;
    int selectedIndex;

    {
        child = new HashMap<>();
        child.put(3, getValues(1000,1200,1200));


        female = new HashMap<>();
        female.put(8, getValues(1300,1500,1600));
        female.put(13, getValues(1500,1800,2000));
        female.put(18, getValues(1800,2000,2400));
        female.put(30, getValues(1900,2100,2400));
        female.put(50, getValues(1800,2000,2200));
        female.put(90, getValues(1600,1800,2200));


        male = new HashMap<>();
        male.put(8, getValues(1300,1500,1800));
        male.put(13, getValues(1800,2000,2300));
        male.put(18, getValues(2200,2600,3000));
        male.put(30, getValues(2500,2700,3000));
        male.put(50, getValues(2300,2500,2900));
        male.put(90, getValues(2100,2300,2600));
    }


    private int getCal(HashMap<Integer,HashMap<String,Integer>> data, int age, String activeness){

        int key_value =
                data.keySet().stream().filter(i -> age<= Integer.parseInt(String.valueOf(i)) )
                        .findFirst()
                        .orElse(-1);

        if(key_value==-1)
            throw new RuntimeException("Cannot find age");

        return Integer.parseInt(String.valueOf(data.get(key_value).get(activeness)));


    }



    public int getRemainingCalories(String gender, int age, String activeness){

        if(age<=3){
            return this.getCal(child, age, activeness);
        }else if(gender.equalsIgnoreCase("male"))
            return this.getCal(male, age, activeness);
        else if(gender.equalsIgnoreCase("female"))
            return this.getCal(female, age, activeness);
        else
            return 0;

    }


    private static HashMap<String,Integer> getValues(int Sedentary, int ModeratelyActive, int Active){

        HashMap<String,Integer> temp = new HashMap<>();
        temp.put(keys.Sedentary.toString(),Sedentary);
        temp.put(keys.ModeratelyActive.toString(),ModeratelyActive);
        temp.put(keys.Active.toString(),Active);

        return temp;
    }


    public  String getFoodName(int inputCal) throws IOException {


        CSVReader reader = new CSVReader(new FileReader("./src/foodList.csv"));
        records = reader.readAll().stream()
                .map(Arrays::asList)
                .map(ArrayList::new)
                .collect(Collectors.toList());
        reader.close();

        matches =  records.stream().skip(1).filter(e->e.get(1).matches("\\d+")&&(
                        Integer.parseInt(e.get(1))-50<=inputCal && inputCal <=Integer.parseInt(e.get(1))+50))
                .collect(Collectors.toCollection(ArrayList::new));

        if(matches.size()==0)
            return "Not enough data to suggest";

        List<Float> feedbackList = matches.stream().map(e->Float.parseFloat((e.get(2)))).collect(Collectors.toCollection(ArrayList::new));
        float max = feedbackList.stream().max(Comparator.comparing(Float::valueOf)).get();
        int count = 1;
        int index = 0;

        if((count= (int) feedbackList.stream().filter(e->(e==max)).count())>1){
            Random rand = new Random();
            index = rand.nextInt(count) ;
        }

        selectedIndex = IntStream.range(0, feedbackList.size())
                .filter(i -> feedbackList.get(i) == max)
                .skip(index)
                .findFirst()
                .orElse(-1);


        System.out.println("Select "+ matches.get(selectedIndex).get(0));
        return  matches.get(selectedIndex).get(0);

    }


    public void setFeedback(boolean feedback, String foodName) throws IOException {

        records =  records.stream().map(e->{
            if(e.get(0).equalsIgnoreCase(foodName)){
                e.set(2,Float.parseFloat(e.get(2))+ ((feedback)? 0.01f: -0.01f)+"");
            }
            return e;}).collect(Collectors.toCollection(ArrayList::new));


        List<String[]> data = records.stream()
                .map(innerList -> innerList.toArray(new String[0]))
                .collect(Collectors.toList());


        try (CSVWriter writer = new CSVWriter(new FileWriter("./src/foodList.csv"))) {
            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

public String getImagePath(){
   return new String("./src/pic/"+matches.get(selectedIndex).get(3)+".png");
}


private static boolean checkInt(String t){
        try{
            Integer.parseInt(t);
            return true;
        }
        catch (Exception e){
            return false;
        }

}





}
