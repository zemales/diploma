import java.util.HashMap;

public enum WeatherEvents {
    C_LOW("Облака нижнего яруса S"),
    C_MID_HIGH("Облака верхнего и среднего яруса С-А"),
    C_CUMULONIMBUS("Кучевые облака Q"),
    RA_LIGHT("Слабые осадки", 18, 29),
    RA_MODERATE("Умеренные осадки", 30, 45),
    RA_HEAVY("Сильные осадки", 46, 63),
    SH_LIGHT("Слабые ливневые осадки", 18, 29),
    SH_MODERATE("Умеренные ливневые осадки", 30, 45),
    SH_HEAVY("Сильные ливневые осадки", 46, 63),
    TS_30_70("Гроза с вероятностью 30-70%"),
    TS_70_85("Гроза с вероятностью 70-85%"),
    TS_85("Гроза с вероятностью >85%"),
    GR_LIGHT("Слабый град"),
    GR_MODERATE("Умеренный град"),
    GR_HEAVY("Сильный град"),
    NULL(null);

    private final String weatherCode;
    private final Integer z1MIN;
    private final Integer z1MAX;

    WeatherEvents(String weatherCode, int z1MIN, int z1MAX) {
        this.weatherCode = weatherCode;
        this.z1MIN = z1MIN;
        this.z1MAX = z1MAX;
    }

    WeatherEvents(String weatherCode) {
        this.weatherCode = weatherCode;
        z1MIN = null;
        z1MAX = null;
    }

    public String getWeatherCode() {
        return this.weatherCode;
    }

    public Integer getZ1MIN() {
        return z1MIN;
    }

    public Integer getZ1MAX() {
        return z1MAX;
    }

    public String rainable() {
        return "'" + RA_LIGHT.getWeatherCode() + "', " +
                "'" + RA_MODERATE.getWeatherCode() + "', " +
                "'" + RA_HEAVY.getWeatherCode() + "', " +
                "'" + SH_LIGHT.getWeatherCode() + "', " +
                "'" + SH_MODERATE.getWeatherCode() + "', " +
                "'" + SH_HEAVY.getWeatherCode() + "'";
    }

    static final HashMap<Integer, String> weatherHashMap = new java.util.HashMap<>();
    static {
        weatherHashMap.put(1, "Облака верхнего и среднего яруса С-А");
        weatherHashMap.put(101, "Облака верхнего и среднего яруса С-А");
        weatherHashMap.put(2, "Кучевые облака Q");
        weatherHashMap.put(102, "Кучевые облака Q");
        weatherHashMap.put(3, "Слабые осадки");
        weatherHashMap.put(103, "Слабые осадки");
        weatherHashMap.put(4, "Слабые ливневые осадки");
        weatherHashMap.put(104, "Слабые ливневые осадки");
        weatherHashMap.put(5, "Умеренные ливневые осадки");
        weatherHashMap.put(14, "Умеренные ливневые осадки");
        weatherHashMap.put(105, "Умеренные ливневые осадки");
        weatherHashMap.put(114, "Умеренные ливневые осадки");
        weatherHashMap.put(6, "Гроза с вероятностью 30-70%");
        weatherHashMap.put(106, "Гроза с вероятностью 30-70%");
        weatherHashMap.put(7, "Слабый град");
        weatherHashMap.put(107, "Слабый град");
        weatherHashMap.put(11, "Облака нижнего яруса S");
        weatherHashMap.put(111, "Облака нижнего яруса S");
        weatherHashMap.put(13, "Умеренные осадки");
        weatherHashMap.put(113, "Умеренные осадки");
        weatherHashMap.put(16, "Гроза с вероятностью 70-85%");
        weatherHashMap.put(116, "Гроза с вероятностью 70-85%");
        weatherHashMap.put(17, "Умеренный град");
        weatherHashMap.put(117, "Умеренный град");
        weatherHashMap.put(23, "Сильные осадки");
        weatherHashMap.put(123, "Сильные осадки");
        weatherHashMap.put(24, "Сильные ливневые осадки");
        weatherHashMap.put(124, "Сильные ливневые осадки");
        weatherHashMap.put(26, "Гроза с вероятностью >85%");
        weatherHashMap.put(126, "Гроза с вероятностью >85%");
        weatherHashMap.put(27, "Сильный град");
        weatherHashMap.put(127, "Сильный град");
        weatherHashMap.put(146, null);
    }
}
