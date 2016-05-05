package co.emes.esuelos.model;

import java.io.Serializable;

/**
 * Created by csarmiento on 22/04/16.
 */
public class Skyline implements Serializable {

    private Integer id;
    private Integer SkylineNumber;
    private Integer depth;
    private Domain hueColor;
    private Domain valueColor;
    private Domain chromaColor;
    private Integer colorPercent;
    private Domain materialType;
    private Domain texture;
    private Domain textureModifiers;
    private Integer texturePercent;
    private Domain structure;
    private Domain skylineType;

    public Skyline() {
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSkylineNumber() {
        return SkylineNumber;
    }

    public void setSkylineNumber(Integer skylineNumber) {
        SkylineNumber = skylineNumber;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Domain getHueColor() {
        return hueColor;
    }

    public void setHueColor(Domain hueColor) {
        this.hueColor = hueColor;
    }

    public Domain getValueColor() {
        return valueColor;
    }

    public void setValueColor(Domain valueColor) {
        this.valueColor = valueColor;
    }

    public Domain getChromaColor() {
        return chromaColor;
    }

    public void setChromaColor(Domain chromaColor) {
        this.chromaColor = chromaColor;
    }

    public Integer getColorPercent() {
        return colorPercent;
    }

    public void setColorPercent(Integer colorPercent) {
        this.colorPercent = colorPercent;
    }

    public Domain getMaterialType() {
        return materialType;
    }

    public void setMaterialType(Domain materialType) {
        this.materialType = materialType;
    }

    public Domain getTexture() {
        return texture;
    }

    public void setTexture(Domain texture) {
        this.texture = texture;
    }

    public Domain getTextureModifiers() {
        return textureModifiers;
    }

    public void setTextureModifiers(Domain textureModifiers) {
        this.textureModifiers = textureModifiers;
    }

    public Integer getTexturePercent() {
        return texturePercent;
    }

    public void setTexturePercent(Integer texturePercent) {
        this.texturePercent = texturePercent;
    }

    public Domain getStructure() {
        return structure;
    }

    public void setStructure(Domain structure) {
        this.structure = structure;
    }

    public Domain getSkylineType() {
        return skylineType;
    }

    public void setSkylineType(Domain skylineType) {
        this.skylineType = skylineType;
    }

    @Override
    public String toString(){
        if(depth!=null && texture!=null){
            return depth + "|" + texture.getDescripcion();
        } else {
            return null;
        }
    }
}
