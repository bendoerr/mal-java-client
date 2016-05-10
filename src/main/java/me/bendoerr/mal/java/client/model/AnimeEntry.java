package me.bendoerr.mal.java.client.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnimeEntry {

    @XmlElement
    private String id;

    @XmlElement
    private String title;

    @XmlElement
    private String english;

    @XmlElement
    private String synonyms;

    @XmlElement
    private String episodes;

    @XmlElement
    private String type;

    @XmlElement
    private String status;

    @XmlElement(name = "start_date")
    private String startDate;

    @XmlElement(name = "end_date")
    private String endDate;

    @XmlElement
    private String synopsis;

    @XmlElement
    private String image;

}
