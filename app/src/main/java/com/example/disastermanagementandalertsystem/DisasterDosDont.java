package com.example.disastermanagementandalertsystem;

import java.util.HashMap;
import java.util.Map;

public class DisasterDosDont {
    Map<String, String> dosMap = new HashMap<>();
    Map<String, String> dontsMap = new HashMap<>();
    DisasterDosDont(){

        dosMap.put("Avalanche","Move to the side of the avalanche path if possible; Protect your head and face with your arms; Keep one arm above the snow to help rescuers locate you; Stay calm and conserve energy");
        dontsMap.put("Avalanche","Don’t try to outrun it downhill; Don’t remove warm clothing; Don’t shout continuously — save energy; Don’t remain in gullies or narrow valleys");

        dosMap.put("Cold Wave","Wear layered warm clothing; Keep emergency blankets ready; Eat high-energy foods and drink warm fluids; Stay indoors during extreme cold");
        dontsMap.put("Cold Wave","Don’t overexert yourself outdoors; Don’t leave pets or livestock without shelter; Don’t ignore frostbite or hypothermia symptoms; Don’t use charcoal stoves indoors without ventilation");

        dosMap.put("Cyclone","Listen to official weather alerts; Secure doors, windows, and loose items; Keep an emergency kit ready; Move to higher ground if advised");
        dontsMap.put("Cyclone","Don’t ignore evacuation orders; Don’t go near the shore to watch waves; Don’t use electrical appliances during flooding; Don’t travel unless necessary");

        dosMap.put("Drought","Conserve water daily; Store water in clean containers; Use drought-resistant crops if farming; Report illegal water wastage");
        dontsMap.put("Drought","Don’t waste water on non-essential uses; Don’t pollute water sources; Don’t burn waste that worsens dryness; Don’t ignore community water-sharing rules");

        dosMap.put("Earthquake","Drop, cover, and hold on during shaking; Stay away from windows and heavy objects; Move to an open space after shaking stops; Keep emergency supplies accessible");
        dontsMap.put("Earthquake","Don’t use elevators; Don’t run outside during strong tremors; Don’t light matches if there’s a gas leak; Don’t stand under doorframes expecting safety");

        dosMap.put("Fire","Raise an alarm immediately; Use a fire extinguisher if safe to do so; Crawl low under smoke; Evacuate quickly via safe routes");
        dontsMap.put("Fire","Don’t open hot doors; Don’t re-enter a burning building; Don’t use water on electrical or oil fires; Don’t ignore fire drills");

        dosMap.put("Flood","Move to higher ground immediately; Listen to official alerts; Turn off electricity and gas before evacuating; Use clean drinking water sources");
        dontsMap.put("Flood","Don’t walk or drive through floodwaters; Don’t ignore evacuation instructions; Don’t drink untreated floodwater; Don’t let children play near water");

        dosMap.put("Gas and Chemical Leakages","Evacuate the area quickly; Cover mouth and nose with a cloth; Follow official emergency instructions; Seek medical help if exposed");
        dontsMap.put("Gas and Chemical Leakages","Don’t touch or smell unknown substances; Don’t use open flames or electrical switches; Don’t stay in low-lying areas where gas collects; Don’t re-enter until declared safe");

        dosMap.put("Thunderstorm","Stay indoors during lightning; Unplug electrical devices; Stay away from tall isolated objects; Follow weather updates");
        dontsMap.put("Thunderstorm","Don’t stand under trees; Don’t use wired telephones during lightning; Don’t swim or bathe during storms; Don’t ignore early warnings");

        dosMap.put("Landslide","Move away from the path of sliding debris; Stay alert after heavy rainfall; Listen to evacuation orders; Keep drainage systems clear");
        dontsMap.put("Landslide","Don’t build near steep slopes; Don’t ignore cracks in the ground; Don’t cross landslide-prone areas during rain; Don’t block natural water flow");

        dosMap.put("Forest Fire","Alert authorities immediately; Evacuate if fire is close; Wear masks to avoid smoke inhalation; Keep flammable materials away from open flames");
        dontsMap.put("Forest Fire","Don’t light campfires in dry forests; Don’t discard cigarette butts carelessly; Don’t try to outrun large fires; Don’t block emergency access roads");

        dosMap.put("Lightning","Stay indoors during lightning strikes; Avoid contact with water and metal; Use battery-operated devices; Crouch low if caught outside in open fields");
        dontsMap.put("Lightning","Don’t shelter under isolated trees; Don’t use electrical appliances; Don’t touch wired plumbing; Don’t lie flat on the ground");

        dosMap.put("Tsunami","Move to higher ground immediately; Follow official tsunami warnings; Keep an emergency kit ready; Help children and elderly evacuate");
        dontsMap.put("Tsunami","Don’t go near the shore to watch waves; Don’t ignore earthquake-related sea changes; Don’t delay evacuation for belongings; Don’t enter water until declared safe");

        dosMap.put("Urban Flood","Avoid waterlogged streets; Keep essential documents in waterproof bags; Use public alerts for safe routes; Turn off home power if flooding occurs");
        dontsMap.put("Urban Flood","Don’t drive into waterlogged areas; Don’t walk barefoot in floodwaters; Don’t throw garbage into drains; Don’t ignore drain blockages");

        dosMap.put("Heat Wave","Drink plenty of water; Wear light, loose clothing; Stay indoors during peak heat; Use wet cloths or cool showers to reduce body temperature");
        dontsMap.put("Heat Wave","Don’t leave children or pets in vehicles; Don’t consume alcohol or caffeine excessively; Don’t overexert during hot hours; Don’t ignore signs of heatstroke");

        dosMap.put("Smog/Air Pollution","Wear N95 masks outdoors; Use air purifiers indoors; Stay indoors on high-pollution days; Drink warm fluids to clear airways");
        dontsMap.put("Smog/Air Pollution","Don’t burn waste materials; Don’t exercise outdoors in heavy smog; Don’t leave windows open during pollution peaks; Don’t ignore breathing difficulties");

        dosMap.put("Biological Emergencies","Follow hygiene protocols strictly; Wear masks and gloves if needed; Stay updated with health advisories; Isolate infected individuals");
        dontsMap.put("Biological Emergencies","Don’t spread rumors or false information; Don’t ignore symptoms of illness; Don’t share personal hygiene items; Don’t break quarantine rules");

        dosMap.put("Nuclear Radiological Emergencies","Move indoors immediately; Seal windows and doors; Follow official radiation safety instructions; Keep emergency supplies and potassium iodide if advised");
        dontsMap.put("Nuclear Radiological Emergencies","Don’t go outdoors unnecessarily; Don’t consume potentially contaminated food or water; Don’t handle unknown metallic debris; Don’t ignore evacuation orders");
    }
}
