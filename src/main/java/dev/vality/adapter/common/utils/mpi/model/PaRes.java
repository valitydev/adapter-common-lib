package dev.vality.adapter.common.utils.mpi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "PARes")
public class PaRes {

    @XmlElement(name = "TX")
    private TX tx;

}
