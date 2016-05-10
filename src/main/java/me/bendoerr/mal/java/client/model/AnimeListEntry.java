package me.bendoerr.mal.java.client.model;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlRootElement(name = "anime")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnimeListEntry {

    @XmlElement(name = "series_animedb_id")
    private String seriesId;

    @XmlElement(name = "series_title")
    private String seriesTitle;

    @XmlElement(name = "series_synonyms")
    private String seriesSynonyms;

    @XmlElement(name = "series_type")
    private String seriesType;

    @XmlElement(name = "series_episodes")
    private String seriesEpisodes;

    @XmlElement(name = "series_status")
    private String seriesStatus;

    @XmlElement(name = "series_start")
    private String seriesStart;

    @XmlElement(name = "series_end")
    private String seriesEnd;

    @XmlElement(name = "series_image")
    private String seriesImage;

    @XmlElement(name = "my_id")
    private String listId;

    @XmlElement(name = "my_watched_episodes")
    private String listWatchedEpisodes;

    @XmlElement(name = "my_start_date")
    private String listStart;

    @XmlElement(name = "my_finish_date")
    private String listFinished;

    @XmlElement(name = "my_score")
    private String listScore;

    @XmlElement(name = "my_status")
    private String listStatus;

    @XmlElement(name = "my_rewatching")
    private String listRewatching;

    @XmlElement(name = "my_rewatching_ep")
    private String listRewatchingEp;

    @XmlElement(name = "my_last_updated")
    private String listLastUpdated;

    @XmlElement(name = "my_tags")
    private String listTags;


    @Data
    @XmlRootElement(name = "myanimelist")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Holder {

        @XmlElement(name = "anime")
        private List<AnimeListEntry> records;

        @XmlAnyElement(lax = true)
        private List<Object> anything;

    }
}
