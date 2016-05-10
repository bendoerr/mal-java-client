package me.bendoerr.mal.java.client.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents values of an anime on a list to be updated.
 * <p>
 * <b>Official API</b>
 * <p>
 * <b>Anime Values (XML)</b>
 * <pre>
 * {@code
 *   <?xml version="1.0" encoding="UTF-8"?>
 *   <entry>
 *     <episode>11</episode>
 *     <status>1</status>
 *     <score>7</score>
 *     <storage_type></storage_type>
 *     <storage_value></storage_value>
 *     <times_rewatched></times_rewatched>
 *     <rewatch_value></rewatch_value>
 *     <date_start></date_start>
 *     <date_finish></date_finish>
 *     <priority></priority>
 *     <enable_discussion></enable_discussion>
 *     <enable_rewatching></enable_rewatching>
 *     <comments></comments>
 *     <fansub_group></fansub_group>
 *     <tags>test tag, 2nd tag</tags>
 *   </entry>
 * }
 * </pre>
 * <b>Parameters</b>
 * <ul>
 *     <li>{@code episode} int</li>
 *     <li>{@code status} int OR string. 1/watching, 2/completed, 3/onhold, 4/dropped, 6/plantowatch</li>
 *     <li>{@code score} int</li>
 *     <li>{@code storage_type} int (will be updated to accomodate strings soon)</li>
 *     <li>{@code storage_value} float</li>
 *     <li>{@code times_rewatched} int</li>
 *     <li>{@code rewatch_value} int</li>
 *     <li>{@code date_start} date. mmddyyyy</li>
 *     <li>{@code date_finish} date. mmddyyyy</li>
 *     <li>{@code priority} int</li>
 *     <li>{@code enable_discussion} int. 1=enable, 0=disable</li>
 *     <li>{@code enable_rewatching} int. 1=enable, 0=disable</li>
 *     <li>{@code comments} string</li>
 *     <li>{@code fansub_group} string</li>
 *     <li>{@code tags} string. tags separated by commas</li>
 * </ul>
 */
@Data
@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnimeListEntryValues {

    @XmlElement
    private String episode;

    @XmlElement
    private String status;

    @XmlElement
    private String score;

    @XmlElement(name = "storage_type")
    private String storageType;

    @XmlElement(name = "storage_value")
    private String storageValue;

    @XmlElement(name = "times_rewatched")
    private String timesRewatched;

    @XmlElement(name = "rewatch_value")
    private String rewatchValue;

    @XmlElement(name = "date_start")
    private String dateStart;

    @XmlElement(name = "date_finish")
    private String dateFinish;

    @XmlElement
    private String priority;

    @XmlElement(name = "enable_discussion")
    private String enableDiscussion;

    @XmlElement(name = "enable_rewatching")
    private String enableRewatching;

    @XmlElement
    private String comments;

    @XmlElement(name = "fansub_group")
    private String fansubGroup;

    @XmlElement
    private String tags;

}
