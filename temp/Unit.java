public class Unit {
    Context mContext;
    Map.Entry<String, ContentValues> entry;
    int initId;
    ContentValues valuesUnit;
    ContentValues valuesCity;


    Unit(Context context, Map.Entry<String, ContentValues> entry, ContentValues valuesCity){
        this.entry = entry;
        mContext = context;
        initId = Integer.valueOf(entry.getKey());
        valuesUnit = entry.getValue();
        this.valuesCity = valuesCity;
        int day = valuesUnit.getAsInteger(UnitTable.KEY_DAYS_COUNT) + 1;
        valuesUnit.put(UnitTable.KEY_DAYS_COUNT, day);
    }


    public void buyGlass() {
        if (valuesUnit.getAsInteger(UnitTable.KEY_CASH) > valuesCity.getAsInteger(CityTable.KEY_PRICE_DOWN)){
            float glassDay = machGlassDay(valuesUnit, valuesCity);
            int cashMinus = (int)(glassDay * valuesCity.getAsInteger(CityTable.KEY_PRICE_DOWN));
            int cash = valuesUnit.getAsInteger(UnitTable.KEY_CASH);
            if (cash >= cashMinus){
                cash -= cashMinus;
            }else {
                glassDay = ((float)cash / valuesCity.getAsInteger(CityTable.KEY_PRICE_DOWN));
                cash = 0;
            }
            glassDay += valuesUnit.getAsFloat(UnitTable.KEY_GLASS_QTY);
            valuesUnit.put(UnitTable.KEY_GLASS_QTY, glassDay);
            valuesUnit.put(UnitTable.KEY_CASH, cash);
            new UnitTable(mContext).updateEntry(Integer.valueOf(entry.getKey()),valuesUnit);
        }
    }

    private float machGlassDay(ContentValues valuesUnit, ContentValues valuesCity){
        //int population = valuesCity.getAsInteger(CityTable.KEY_POPULATION);     //население
        //float kPopulation = valuesCity.getAsInteger(CityTable.KEY_K_POPULATION);    // килограмм на душу населения
        final int NOMINAL_GLASS = 300000;
        final int R_DAYS = 60;

        float r = valuesUnit.getAsInteger(UnitTable.KEY_DAYS_COUNT);
        if (r < R_DAYS){
            r /= R_DAYS;
        }else
            r = 1;

        float glassDay = valuesCity.getAsInteger(CityTable.KEY_G_REAL)/valuesCity.getAsInteger(CityTable.KEY_UNIT_QTY);
        if (glassDay > NOMINAL_GLASS)
            glassDay = NOMINAL_GLASS;
        glassDay /= 30;
        return  (glassDay/1000) * r;//переводим в тоны
    }

    public void saleGlass() {
        ContentValues values = entry.getValue();
        int rate = values.getAsInteger(UnitTable.KEY_RATE);
    }

    public void addCash(int cash){}

    public int takeCash(){
        return 0;
    }
}