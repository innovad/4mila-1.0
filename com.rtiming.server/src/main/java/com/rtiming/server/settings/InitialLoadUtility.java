package com.rtiming.server.settings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.TypeCastUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.AdditionalInformationUtility;
import com.rtiming.shared.dao.RtClassAge;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey_;
import com.rtiming.shared.dao.RtUc_;
import com.rtiming.shared.dao.RtUcl;
import com.rtiming.shared.dao.RtUclKey;
import com.rtiming.shared.dao.RtUclKey_;
import com.rtiming.shared.dao.RtUcl_;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.city.ICountryProcessService;
import com.rtiming.shared.settings.clazz.IAgeProcessService;
import com.rtiming.shared.settings.currency.CurrencyCodeType;
import com.rtiming.shared.settings.currency.CurrencyFormData;
import com.rtiming.shared.settings.currency.ICurrencyProcessService;
import com.rtiming.shared.settings.user.LanguageCodeType;

public final class InitialLoadUtility {

  private InitialLoadUtility() {
  }

  public static void createCodes() throws ProcessingException {
    AdditionalInformationUtility.createStartTimeWish(null);
  }

  public static void createClasses() throws ProcessingException {

    // Women
    createClass("D10", "Women -10", "Damen -10", SexCodeType.WomanCode.ID, 0L, 10L);
    createClass("D12", "Women -12", "Damen -12", SexCodeType.WomanCode.ID, 0L, 12L);
    createClass("D14", "Women -14", "Damen -14", SexCodeType.WomanCode.ID, 0L, 14L);
    createClass("D16", "Women -16", "Damen -16", SexCodeType.WomanCode.ID, 0L, 16L);
    createClass("D18", "Women -18", "Damen -18", SexCodeType.WomanCode.ID, 0L, 18L);
    createClass("D20", "Women -20", "Damen -20", SexCodeType.WomanCode.ID, 0L, 20L);

    createClass("DE", "Women Elite", "Damen Elite", SexCodeType.WomanCode.ID, 0L, null);
    createClass("DAL", "Women A Long", "Damen A Lang", SexCodeType.WomanCode.ID, 0L, null);
    createClass("DAM", "Women A Medium", "Damen A Mittel", SexCodeType.WomanCode.ID, 0L, null);
    createClass("DAK", "Women A Short", "Damen A Kurz", SexCodeType.WomanCode.ID, 0L, null);
    createClass("DB", "Women B", "Damen B", SexCodeType.WomanCode.ID, 0L, null);

    createClass("D35", "Women 35-", "Damen 35-", SexCodeType.WomanCode.ID, 35L, null);
    createClass("D40", "Women 40-", "Damen 40-", SexCodeType.WomanCode.ID, 40L, null);
    createClass("D45", "Women 45-", "Damen 45-", SexCodeType.WomanCode.ID, 45L, null);
    createClass("D50", "Women 50-", "Damen 50-", SexCodeType.WomanCode.ID, 50L, null);
    createClass("D55", "Women 55-", "Damen 55-", SexCodeType.WomanCode.ID, 55L, null);
    createClass("D60", "Women 60-", "Damen 60-", SexCodeType.WomanCode.ID, 60L, null);
    createClass("D65", "Women 65-", "Damen 65-", SexCodeType.WomanCode.ID, 65L, null);
    createClass("D70", "Women 70-", "Damen 70-", SexCodeType.WomanCode.ID, 70L, null);
    createClass("D75", "Women 75-", "Damen 75-", SexCodeType.WomanCode.ID, 75L, null);
    createClass("D80", "Women 80-", "Damen 80-", SexCodeType.WomanCode.ID, 80L, null);
    createClass("D85", "Women 85-", "Damen 85-", SexCodeType.WomanCode.ID, 85L, null);

    // Man
    createClass("H10", "Men -10", "Herren -10", null, 0L, 10L);
    createClass("H12", "Men -12", "Herren -12", null, 0L, 12L);
    createClass("H14", "Men -14", "Herren -14", null, 0L, 14L);
    createClass("H16", "Men -16", "Herren -16", null, 0L, 16L);
    createClass("H18", "Men -18", "Herren -18", null, 0L, 18L);
    createClass("H20", "Men -20", "Herren -20", null, 0L, 20L);

    createClass("HE", "Men Elite", "Herren Elite", null, 0L, null);
    createClass("HAL", "Men A Long", "Herren A Lang", null, 0L, null);
    createClass("HAM", "Men A Medium", "Herren A Mittel", null, 0L, null);
    createClass("HAK", "Men A Short", "Herren A Kurz", null, 0L, null);
    createClass("HB", "Men B", "Herren B", null, 0L, null);

    createClass("H35", "Men 35-", "Herren 35-", null, 35L, null);
    createClass("H40", "Men 40-", "Herren 40-", null, 40L, null);
    createClass("H45", "Men 45-", "Herren 45-", null, 45L, null);
    createClass("H50", "Men 50-", "Herren 50-", null, 50L, null);
    createClass("H55", "Men 55-", "Herren 55-", null, 55L, null);
    createClass("H60", "Men 60-", "Herren 60-", null, 60L, null);
    createClass("H65", "Men 65-", "Herren 65-", null, 65L, null);
    createClass("H70", "Men 70-", "Herren 70-", null, 70L, null);
    createClass("H75", "Men 75-", "Herren 75-", null, 75L, null);
    createClass("H80", "Men 80-", "Herren 80-", null, 80L, null);
    createClass("H85", "Men 85-", "Herren 85-", null, 85L, null);

  }

  //CHECKSTYLE:OFF
  public static void createCountries() throws ProcessingException {
    //CHECKSTYLE:ON

    createCountry("AF", "AFG", "Afghanistan", "Afghanistan");
    createCountry("AL", "ALB", "Albania", "Albanien");
    createCountry("DZ", "ALG", "Algeria", "Algerien");
    createCountry("AS", "ASA", "American Samoa", "Amerikanisch-Samoa");
    createCountry("AD", "AND", "Andorra", "Andorra");
    createCountry("AO", "ANG", "Angola", "Angola");
    createCountry("AG", "ANT", "Antigua and Barbuda", "Antigua und Barbuda");
    createCountry("AR", "ARG", "Argentina", "Argentinien");
    createCountry("AM", "ARM", "Armenia", "Armenien");
    createCountry("AW", "ARU", "Aruba", "Aruba");
    createCountry("AU", "AUS", "Australia", "Australien");
    createCountry("AT", "AUT", "Austria", "Österreich");
    createCountry("AZ", "AZE", "Azerbaijan", "Aserbeidschan");
    createCountry("BS", "BAH", "Bahamas", "Bahamas");
    createCountry("BH", "BRN", "Bahrain", "Bahrein");
    createCountry("BD", "BAN", "Bangladesh", "Bangladesch");
    createCountry("BB", "BAR", "Barbados", "Barbados");
    createCountry("BY", "BLR", "Belarus", "Weissrussland");
    createCountry("BE", "BEL", "Belgium", "Belgien");
    createCountry("BZ", "BIZ", "Belize", "Belize");
    createCountry("BJ", "BEN", "Benin", "Benin");
    createCountry("BM", "BER", "Bermuda", "Bermudas");
    createCountry("BT", "BHU", "Bhutan", "Bhutan");
    createCountry("BO", "BOL", "Bolivia", "Bolivien");
    createCountry("BA", "BIH", "Bosnia and Herzegovina", "Bosnien und Herzegowina");
    createCountry("BW", "BOT", "Botswana", "Botswana");
    createCountry("BR", "BRA", "Brazil", "Brasilien");
    createCountry("BN", "BRU", "Brunei", "Brunei");
    createCountry("BG", "BUL", "Bulgaria", "Bulgarien");
    createCountry("BF", "BUR", "Burkina Faso", "Burkina Faso");
    createCountry("BI", "BDI", "Burundi", "Burundi");
    createCountry("KH", "CAM", "Cambodia", "Kambodscha");
    createCountry("CM", "CMR", "Cameroon", "Kamerun");
    createCountry("CA", "CAN", "Canada", "Kanada");
    createCountry("CV", "CPV", "Cape Verde", "Kapverden");
    createCountry("KY", "CAY", "Cayman Islands", "Kayman-Inseln");
    createCountry("CF", "CAF", "Central African Republic", "Zentralafrikanische Republik");
    createCountry("TD", "CHA", "Chad", "Tschad");
    createCountry("CL", "CHI", "Chile", "Chile");
    createCountry("CN", "CHN", "China", "China");
    createCountry("CO", "COL", "Colombia", "Kolumbien");
    createCountry("KM", "COM", "Comoros", "Komoren");
    createCountry("CG", "CGO", "Congo (Republic)", "Kongo (Republik)");
    createCountry("CD", "COD", "Congo (Democratic Republic)", "Kongo (Demokratische Republik)");
    createCountry("CK", "COK", "Cook Islands", "Cook-Inseln");
    createCountry("CR", "CRC", "Costa Rica", "Costa Rica");
    createCountry("HR", "CRO", "Croatia", "Kroatien");
    createCountry("CU", "CUB", "Cuba", "Kuba");
    createCountry("CY", "CYP", "Cyprus", "Zypern");
    createCountry("CZ", "CZE", "Czech Republic", "Tschechische Republik");
    createCountry("CI", "CIV", "Côte D'Ivoire", "Elfenbeinküste");
    createCountry("DK", "DEN", "Denmark", "Dänemark");
    createCountry("DJ", "DJI", "Djibouti", "Dschibuti");
    createCountry("DM", "DMA", "Dominica", "Dominica");
    createCountry("DO", "DOM", "Dominican Republic", "Dominikanische Republik");
    createCountry("TL", "TLS", "East Timor (Timor-Leste)", "Ost-Timor");
    createCountry("EC", "ECU", "Ecuador", "Equador");
    createCountry("EG", "EGY", "Egypt", "Ägypten");
    createCountry("SV", "ESA", "El Salvador", "El Salvador");
    createCountry("GQ", "GEQ", "Equatorial Guinea", "Äquatorial-Guinea");
    createCountry("ER", "ERI", "Eritrea", "Eritrea");
    createCountry("EE", "EST", "Estonia", "Estland");
    createCountry("ET", "ETH", "Ethiopia", "Äthiopien");
    createCountry("FJ", "FIJ", "Fiji", "Fidschi");
    createCountry("FI", "FIN", "Finland", "Finnland");
    createCountry("FR", "FRA", "France", "Frankreich");
    createCountry("GA", "GAB", "Gabon", "Gabon");
    createCountry("GM", "GAM", "Gambia", "Gambia");
    createCountry("GE", "GEO", "Georgia", "Georgien");
    createCountry("DE", "GER", "Germany", "Deutschland");
    createCountry("GH", "GHA", "Ghana", "Ghana");
    createCountry("GR", "GRE", "Greece", "Griechenland");
    createCountry("GD", "GRN", "Grenada", "Grenada");
    createCountry("GU", "GUM", "Guam", "Guam");
    createCountry("GT", "GUA", "Guatemala", "Guatemala");
    createCountry("GN", "GUI", "Guinea", "Guinea");
    createCountry("GW", "GBS", "Guinea-Bissau", "Guinea-Bissau");
    createCountry("GY", "GUY", "Guyana", "Guyana");
    createCountry("HT", "HAI", "Haiti", "Haiti");
    createCountry("VA", "VAT", "Vatican", "Vatikan");
    createCountry("HN", "HON", "Honduras", "Honduras");
    createCountry("HK", "HKG", "Hong Kong", "Hong Kong");
    createCountry("HU", "HUN", "Hungary", "Ungarn");
    createCountry("IS", "ISL", "Iceland", "Island");
    createCountry("IN", "IND", "India", "Indien");
    createCountry("ID", "INA", "Indonesia", "Indonesien");
    createCountry("IR", "IRI", "Iran", "Iran");
    createCountry("IQ", "IRQ", "Iraq", "Irak");
    createCountry("IE", "IRL", "Ireland", "Irland");
    createCountry("IL", "ISR", "Israel", "Israel");
    createCountry("IT", "ITA", "Italy", "Italien");
    createCountry("JM", "JAM", "Jamaica", "Jamaika");
    createCountry("JP", "JPN", "Japan", "Japan");
    createCountry("JO", "JOR", "Jordan", "Jordanien");
    createCountry("KZ", "KAZ", "Kazakhstan", "Kasachstan");
    createCountry("KE", "KEN", "Kenya", "Kenia");
    createCountry("KP", "PRK", "Korea (PDR)", "Korea (Demokratische Volksrepublik)");
    createCountry("KR", "KOR", "Korea (Republic)", "Korea (Republik)");
    createCountry("KW", "KUW", "Kuwait", "Kuwait");
    createCountry("KG", "KGZ", "Kyrgyzstan", "Kirgistan");
    createCountry("LA", "LAO", "Laos", "Laso");
    createCountry("LV", "LAT", "Latvia", "Lettland");
    createCountry("LB", "LIB", "Lebanon", "Libanon");
    createCountry("LS", "LES", "Lesotho", "Lesotho");
    createCountry("LR", "LBR", "Liberia", "Liberia");
    createCountry("LY", "LBA", "Libya", "Lybien");
    createCountry("LI", "LIE", "Liechtenstein", "Liechtenstein");
    createCountry("LT", "LTU", "Lithuania", "Litauen");
    createCountry("LU", "LUX", "Luxembourg", "Luxemburg");
    createCountry("MK", "MKD", "Macedonia", "Mazedonien");
    createCountry("MG", "MAD", "Madagascar", "Madagaskar");
    createCountry("MW", "MAW", "Malawi", "Malawi");
    createCountry("MY", "MAS", "Malaysia", "Malaysia");
    createCountry("MV", "MDV", "Maldives", "Malediven");
    createCountry("ML", "MLI", "Mali", "Mali");
    createCountry("MT", "MLT", "Malta", "Malta");
    createCountry("MR", "MTN", "Mauritania", "Mauretanien");
    createCountry("MU", "MRI", "Mauritius", "Mauritius");
    createCountry("MX", "MEX", "Mexico", "Mexiko");
    createCountry("FM", "FSM", "Micronesia", "Mikronesien");
    createCountry("MD", "MDA", "Moldova", "Moldawien");
    createCountry("MC", "MON", "Monaco", "Monaco");
    createCountry("MN", "MGL", "Mongolia", "Mongolei");
    createCountry("ME", "MNG", "Montenegro", "Montenegro");
    createCountry("MA", "MAR", "Morocco", "Marokko");
    createCountry("MZ", "MOZ", "Mozambique", "Moçambique");
    createCountry("MM", "MYA", "Myanmar (Burma)", "Myanmar (Burma)");
    createCountry("NA", "NAM", "Namibia", "Namibia");
    createCountry("NR", "NRU", "Nauru", "Nauru");
    createCountry("NP", "NEP", "Nepal", "Nepal");
    createCountry("NL", "NED", "Netherlands", "Niederlande");
    createCountry("AN", "AHO", "Netherlands Antilles", "Niederländische Antillen");
    createCountry("NZ", "NZL", "New Zealand", "Neuseeland");
    createCountry("NI", "NCA", "Nicaragua", "Nicaragua");
    createCountry("NE", "NIG", "Niger", "Niger");
    createCountry("NG", "NGR", "Nigeria", "Nigeria");
    createCountry("NO", "NOR", "Norway", "Norwegen");
    createCountry("OM", "OMA", "Oman", "Oman");
    createCountry("PK", "PAK", "Pakistan", "Pakistan");
    createCountry("PW", "PLW", "Palau", "Palau");
    createCountry("PS", "PLE", "Palestine", "Palästina");
    createCountry("PA", "PAN", "Panama", "Panama");
    createCountry("PG", "PNG", "Papua New Guinea", "Papua Neu-Guinea");
    createCountry("PY", "PAR", "Paraguay", "Paraguay");
    createCountry("PE", "PER", "Peru", "Peru");
    createCountry("PH", "PHI", "Philippines", "Philippinen");
    createCountry("PL", "POL", "Poland", "Polen");
    createCountry("PT", "POR", "Portugal", "Portugal");
    createCountry("PR", "PUR", "Puerto Rico", "Puerto Rico");
    createCountry("QA", "QAT", "Qatar", "Qatar");
    createCountry("RO", "ROU", "Romania", "Rumänien");
    createCountry("RU", "RUS", "Russia", "Russland");
    createCountry("RW", "RWA", "Rwanda", "Ruanda");
    createCountry("KN", "SKN", "Saint Kitts and Nevis", "St. Kitts und Nevis");
    createCountry("LC", "LCA", "Saint Lucia", "St. Lucia");
    createCountry("VC", "VIN", "Saint Vincent and the Grenadines", "St. Vincent und die Grenadinen");
    createCountry("WS", "SAM", "Samoa", "Samoa");
    createCountry("SM", "SMR", "San Marino", "San Marino");
    createCountry("ST", "STP", "Sao Tome and Principe", "Sao Tome and Principe");
    createCountry("SA", "KSA", "Saudi Arabia", "Saudi-Arabien");
    createCountry("SN", "SEN", "Senegal", "Senegal");
    createCountry("RS", "SRB", "Serbia", "Serbien");
    createCountry("SC", "SEY", "Seychelles", "Seychelles");
    createCountry("SL", "SLE", "Sierra Leone", "Sierra Leone");
    createCountry("SG", "SIN", "Singapore", "Singapur");
    createCountry("SK", "SVK", "Slovakia", "Slowakei");
    createCountry("SI", "SLO", "Slovenia", "Slovenien");
    createCountry("SB", "SOL", "Solomon Islands", "Solomon-Inseln");
    createCountry("SO", "SOM", "Somalia", "Somalia");
    createCountry("ZA", "RSA", "South Africa", "Südafrika");
    createCountry("SS", "SSD", "South Sudan", "Südsudan");
    createCountry("ES", "ESP", "Spain", "Spanien");
    createCountry("LK", "SRI", "Sri Lanka", "Sri Lanka");
    createCountry("SD", "SUD", "Sudan", "Sudan");
    createCountry("SR", "SUR", "Suriname", "Surinam");
    createCountry("SZ", "SWZ", "Swaziland", "Swaziland");
    createCountry("SE", "SWE", "Sweden", "Schweden");
    CountryFormData defaultCountry = createCountry("CH", "SUI", "Switzerland", "Schweiz");
    createCountry("SY", "SYR", "Syria", "Syrien");
    createCountry("TW", "TPE", "Taiwan", "Taiwan");
    createCountry("TJ", "TJK", "Tajikistan", "Tadschikistan");
    createCountry("TZ", "TAN", "Tanzania", "Tansania");
    createCountry("TH", "THA", "Thailand", "Thailand");
    createCountry("TG", "TOG", "Togo", "Togo");
    createCountry("TO", "TGA", "Tonga", "Tonga");
    createCountry("TT", "TRI", "Trinidad and Tobago", "Trinidad und Tobago");
    createCountry("TN", "TUN", "Tunisia", "Tunesien");
    createCountry("TR", "TUR", "Turkey", "Türkei");
    createCountry("TM", "TKM", "Turkmenistan", "Turkmenistan");
    createCountry("UG", "UGA", "Uganda", "Uganda");
    createCountry("UA", "UKR", "Ukraine", "Ukraine");
    createCountry("AE", "UAE", "United Arab Emirates", "Vereinigte Arabische Emirate");
    createCountry("GB", "GBR", "United Kingdom", "Grossbritannien");
    createCountry("US", "USA", "United States", "Vereinigte Staaten von Amerika");
    createCountry("UY", "URU", "Uruguay", "Uruguay");
    createCountry("UZ", "UZB", "Uzbekistan", "Usbekistan");
    createCountry("VU", "VAN", "Vanuatu", "Vanuatu");
    createCountry("VE", "VEN", "Venezuela", "Venezuela");
    createCountry("VN", "VIE", "Vietnam", "Vietnam");
    createCountry("VG", "IVB", "British Virgin Islands", "Britische Jungferninseln");
    createCountry("VI", "ISV", "American Virgin Islands", "Amerikanische Jungferninseln");
    createCountry("WF", "WAF", "Wallis and Futuna", "Wallis und Futuna");
    createCountry("YE", "YEM", "Yemen", "Jemen");
    createCountry("ZM", "ZAM", "Zambia", "Sambia");
    createCountry("ZW", "ZIM", "Zimbabwe", "Simbabwe");

    BEANS.get(IDefaultProcessService.class).setDefaultCountryUid(defaultCountry.getCountryUid());

    /* ARG=Argentina, AUS=Australia, AUT=Austria, BAR=Barbados, BEL=Belgium,
     * BLR=Belarus, BRA=Brazil, BUL=Bulgaria,  CAN=Canada, CHI=Chile,
     * CHN=China, COL=Colombia, CRO=Croatia, CUB=Cuba, CZE=Czech Republic,
     * DEN=Denmark, ECU=Ecuador, ESP= Spain, EST=Estonia, FIN=Finland, FRA=France,
     * GBR=Great Britain, GEO=Georgia, GER=Germany, GRE=Greece, HKG=Hong Kong,
     * HUN=Hungary, INA=Indonesia, IND=India, IRL=Ireland, ISR=Israel,
     * ITA=Italy, JAM=Jamaica, JPN=Japan, KAZ=Kazakhstan, KEN=Kenya, KGZ=Kyrgyzstan,
     * KOR=Korea, LAT=Latvia, LIE=Liechtenstein, LTU=Lithuania, MAS=Malaysia,
     * MDA=Moldova, MGL=Mongolia, MKD=Macedonia, MNE=Montenegro, MOZ=Mozambique,
     * NED=Netherlands, NOR=Norway, NZL=New Zealand, PAK=Pakistan, PAN=Panama,
     * POL=Poland, POR=Portugal, PRK=DPR Korea, PUR=Puerto Rico, ROU=Romania,
     * RSA=South Africa, RUS=Russia, SRB=Republic Serbia, SLO=Slovenia, SOM=Somalia,
     * SUI=Switzerland, SVK=Slovakia, SWE=Sweden, THA=Thailand, TPE=Chinese Taipei,
     * TUR=Turkey, UKR=Ukraine, URU=Uruguay, USA=United States, VEN=Venezuela
     *
    */

  }

  public static void createCurrencies() throws ProcessingException {
    // http://www.geopostcodes.com/de/GeoPC_ISO_4217_Currencies_codes

    createCurrency("AED", "د.إ");
    createCurrency("AFN", "؋");
    createCurrency("ALL", "L");
    createCurrency("AMD", "դր.");
    createCurrency("ANG", "NAƒ");
    createCurrency("AOA", "Kz");
    createCurrency("ARS", "$");
    createCurrency("AUD", "$");
    createCurrency("AWG", "Afl.");
    createCurrency("AZN", "ман");
    createCurrency("BAM", "KM");
    createCurrency("BBD", "Bds$");
    createCurrency("BDT", "৳");
    createCurrency("BGN", "лв");
    createCurrency("BHD", ".د.ب");
    createCurrency("BIF", "FBu");
    createCurrency("BMD", "BD$");
    createCurrency("BND", "B$");
    createCurrency("BOB", "Bs.");
    createCurrency("BRL", "R$");
    createCurrency("BSD", "B$");
    createCurrency("BTN", "Nu.");
    createCurrency("BWP", "P");
    createCurrency("BYR", "Br");
    createCurrency("BZD", "BZ$");
    createCurrency("CAD", "$");
    createCurrency("CDF", "F");
    createCurrency("CHF", "Fr.");
    createCurrency("CLP", "$");
    createCurrency("CNY", "¥");
    createCurrency("COP", "Col$");
    createCurrency("CRC", "₡");
    createCurrency("CUP", "$");
    createCurrency("CVE", "Esc");
    createCurrency("CZK", "Kč");
    createCurrency("DJF", "Fdj");
    createCurrency("DKK", "Kr");
    createCurrency("DOP", "RD$");
    createCurrency("DZD", "دج");
    createCurrency("EEK", "KR");
    createCurrency("EGP", "£");
    createCurrency("ERN", "Nfk");
    createCurrency("ETB", "Br");
    createCurrency("EUR", "€");
    createCurrency("FJD", "FJ$");
    createCurrency("FKP", "£");
    createCurrency("GBP", "£");
    createCurrency("GEL", "lari");
    createCurrency("GHS", "GH₵");
    createCurrency("GIP", "£");
    createCurrency("GMD", "D");
    createCurrency("GNF", "FG");
    createCurrency("GTQ", "Q");
    createCurrency("GWP", "$");
    createCurrency("GYD", "GY$");
    createCurrency("HKD", "HK$");
    createCurrency("HNL", "L");
    createCurrency("HRK", "kn");
    createCurrency("HTG", "G");
    createCurrency("HUF", "Ft");
    createCurrency("IDR", "Rp");
    createCurrency("ILS", "₪");
    createCurrency("INR", "Rs");
    createCurrency("IQD", "ع.د");
    createCurrency("IRR", "﷼");
    createCurrency("ISK", "kr");
    createCurrency("JMD", "$");
    createCurrency("JOD", "دينار");
    createCurrency("JPY", "¥");
    createCurrency("KES", "KSh");
    createCurrency("KGS", "сом");
    createCurrency("KHR", "riel");
    createCurrency("KMF", "Fr.");
    createCurrency("KPW", "₩");
    createCurrency("KRW", "₩");
    createCurrency("KWD", "د.ك");
    createCurrency("KYD", "$");
    createCurrency("KZT", "〒");
    createCurrency("LAK", "₭");
    createCurrency("LBP", "ل.ل");
    createCurrency("LKR", "Rs");
    createCurrency("LRD", "L$");
    createCurrency("LSL", "L");
    createCurrency("LTL", "Lt");
    createCurrency("LVL", "Ls");
    createCurrency("LYD", "ل.د");
    createCurrency("MAD", "د.م");
    createCurrency("MDL", "leu");
    createCurrency("MGA", "FMG");
    createCurrency("MKD", "ден");
    createCurrency("MMK", "K");
    createCurrency("MNT", "₮");
    createCurrency("MOP", "MOP$");
    createCurrency("MRO", "UM");
    createCurrency("MUR", "Rs");
    createCurrency("MVR", "Rf");
    createCurrency("MWK", "MK");
    createCurrency("MXN", "$");
    createCurrency("MYR", "RM");
    createCurrency("MZN", "MTn");
    createCurrency("NAD", "N$");
    createCurrency("NGN", "₦");
    createCurrency("NIO", "C$");
    createCurrency("NOK", "kr");
    createCurrency("NPR", "NRs");
    createCurrency("NZD", "$");
    createCurrency("OMR", "ر.ع.");
    createCurrency("PAB", "B./");
    createCurrency("PEN", "S/.");
    createCurrency("PGK", "K");
    createCurrency("PHP", "₱");
    createCurrency("PKR", "Rs.");
    createCurrency("PLN", "zł");
    createCurrency("PYG", "₲");
    createCurrency("QAR", "ر.ق");
    createCurrency("RON", "L");
    createCurrency("RSD", "дин.");
    createCurrency("RUB", "руб");
    createCurrency("RWF", "RF");
    createCurrency("SAR", "ر.س");
    createCurrency("SBD", "SI$");
    createCurrency("SCR", "SR");
    createCurrency("SDG", "£");
    createCurrency("SEK", "kr");
    createCurrency("SGD", "S$");
    createCurrency("SHP", "£");
    createCurrency("SLL", "Le");
    createCurrency("SOS", "So.");
    createCurrency("SRD", "$");
    createCurrency("STD", "Db");
    createCurrency("SVC", "₡");
    createCurrency("SYP", "S£");
    createCurrency("SZL", "L");
    createCurrency("THB", "฿");
    createCurrency("TJS", "Сом");
    createCurrency("TMT", "m");
    createCurrency("TND", "د.ت");
    createCurrency("TOP", "T$");
    createCurrency("TRY", "TL");
    createCurrency("TTD", "$");
    createCurrency("TWD", "$");
    createCurrency("TZS", "/=");
    createCurrency("UAH", "₴");
    createCurrency("UGX", "USh");
    createCurrency("USD", "$");
    createCurrency("UYU", "$");
    createCurrency("UZS", "сўм");
    createCurrency("VEF", "Bs.");
    createCurrency("VND", "₫");
    createCurrency("VUV", "Vt");
    createCurrency("WST", "WS$");
    createCurrency("XAF", "CFA");
    createCurrency("XCD", "EC$");
    createCurrency("XOF", "CFA");
    createCurrency("XPF", "F");
    createCurrency("YER", "ريال");
    createCurrency("ZAR", "R");
    createCurrency("ZMK", "ZK");
    createCurrency("ZWL", "$");
  }

  public static long insertMissingTranslationsWithEnglish() throws ProcessingException {
    long insertCount = 0;
    for (ICode code : CODES.getCodeType(LanguageCodeType.class).getCodes()) {
      if (CompareUtility.notEquals(LanguageCodeType.English.ID, code.getId())) {
        Long otherLanguageUid = (Long) code.getId();

        Map<Long, String> language = selectTranslations(otherLanguageUid);
        Map<Long, String> english = selectTranslations(LanguageCodeType.English.ID);

        for (Long ucUid : english.keySet()) {
          if (!language.keySet().contains(ucUid)) {
            RtUcl ucl = new RtUcl();
            RtUclKey key = new RtUclKey();
            key.setClientNr(ServerSession.get().getSessionClientNr());
            key.setLanguageUid(otherLanguageUid);
            key.setUcUid(ucUid);
            ucl.setId(key);
            ucl.setCodeName(english.get(ucUid));
            JPA.persist(ucl);
            insertCount++;
          }
        }
      }
    }
    return insertCount;
  }

  private static Map<Long, String> selectTranslations(Long languageUid) {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtUc> uc = selectQuery.from(RtUc.class);
    Join<RtUc, RtUcl> joinUcl = uc.join(RtUc_.rtUcls, JoinType.INNER);

    selectQuery.select(b.array(
        uc.get(RtUc_.id).get(RtUcKey_.ucUid),
        joinUcl.get(RtUcl_.codeName)
        ))
        .where(
            b.and(
                b.equal(uc.get(RtUc_.id).get(RtUcKey_.clientNr), ServerSession.get().getSessionClientNr()),
                b.equal(joinUcl.get(RtUcl_.id).get(RtUclKey_.languageUid), languageUid)
                )
        );
    List<Object[]> result = JPA.createQuery(selectQuery).getResultList();
    Map<Long, String> map = new HashMap<Long, String>();
    for (Object[] row : result) {
      Long ucUid = TypeCastUtility.castValue(row[0], Long.class);
      String codeName = TypeCastUtility.castValue(row[1], String.class);
      map.put(ucUid, codeName);
    }
    return map;
  }

  private static void createCurrency(String code, String text) throws ProcessingException {
    CurrencyFormData currency = new CurrencyFormData();
    ICurrencyProcessService currencyService = BEANS.get(ICurrencyProcessService.class);
    CodeFormData codeFormData = BEANS.get(ICodeProcessService.class).find(code, CurrencyCodeType.ID);
    if (codeFormData.getCodeUid() == null) {
      currency = currencyService.prepareCreate(currency);
    }
    else {
      currency.setCurrencyUid(codeFormData.getCodeUid());
      currency = currencyService.load(currency);
    }
    currency.getCodeBox().getShortcut().setValue(code);
    currency.getCodeBox().getActive().setValue(true);
    currency.getExchangeRate().setValue(1d);
    for (int i = 0; i < currency.getCodeBox().getLanguage().getRowCount(); i++) {
      currency.getCodeBox().getLanguage().setTranslation(i, text);
    }
    if (codeFormData.getCodeUid() == null) {
      currencyService.create(currency);
    }
    else {
      currencyService.store(currency);
    }
  }

  protected static void createClass(String classShortcut, String translationDefault, String translationGerman, Long sexUid, Long ageFrom, Long ageTo) throws ProcessingException {
    // Class
    CodeFormData clazz = BEANS.get(ICodeProcessService.class).find(classShortcut, ClassCodeType.ID);
    for (int i = 0; i < clazz.getMainBox().getLanguage().getRowCount(); i++) {
      if (CompareUtility.equals(clazz.getMainBox().getLanguage().getLanguage(i), LanguageCodeType.German.ID)) {
        clazz.getMainBox().getLanguage().setTranslation(i, translationGerman);
      }
      else {
        clazz.getMainBox().getLanguage().setTranslation(i, translationDefault);
      }
    }
    if (clazz.getCodeUid() == null) {
      BEANS.get(ICodeProcessService.class).create(clazz);
    }
    else {
      BEANS.get(ICodeProcessService.class).store(clazz);
    }

    // Age Class
    String queryString = "DELETE FROM RtClassAge WHERE classUid = :classUid AND id.clientNr = :clientNr";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("classUid", clazz.getCodeUid());
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();

    RtClassAge age = new RtClassAge();
    age.setClassUid(clazz.getCodeUid());
    age.setAgeFrom(ageFrom);
    age.setAgeTo(ageTo);
    age.setSexUid(sexUid);

    age = BEANS.get(IAgeProcessService.class).create(age);
  }

  private static CountryFormData createCountry(String shortcut, String nationCode, String translationDefault, String translationGerman) throws ProcessingException {
    translationGerman = StringUtility.nvl(translationGerman, translationDefault);

    // Find Country
    CountryFormData country = BEANS.get(ICountryProcessService.class).find(translationDefault, shortcut, nationCode);
    for (int i = 0; i < country.getCodeBox().getLanguage().getRowCount(); i++) {
      if (CompareUtility.equals(country.getCodeBox().getLanguage().getLanguage(i), LanguageCodeType.German.ID)) {
        country.getCodeBox().getLanguage().setTranslation(i, translationGerman);
      }
      else {
        country.getCodeBox().getLanguage().setTranslation(i, translationDefault);
      }
    }
    if (country.getCountryUid() == null) {
      country = BEANS.get(ICountryProcessService.class).create(country);
    }
    else {
      BEANS.get(ICountryProcessService.class).store(country);
    }

    return country;
  }

}
