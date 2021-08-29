import org.junit.Assert;

public class ColorMapTest {

    @org.junit.Test
    public void readFromFile() {
    }

    @org.junit.Test
    public void insertIntoCollection() {
    }

    @org.junit.Test
    public void whatExistState() {
        ColorMap clrMap = new ColorMap();
        clrMap.InsertIntoCollection(new String[]{"WA", "ID", "OR"});
        clrMap.InsertIntoCollection(new String[]{"ID", "MT", "WY", "UT", "NV", "OR", "WA"});
        clrMap.InsertIntoCollection(new String[]{"OR","NV","CA","WA"});
        clrMap.InsertIntoCollection(new String[]{"MT","ND","SD","WY","ID"});
        clrMap.InsertIntoCollection(new String[]{"ND","MN","SD","MT"});
        clrMap.InsertIntoCollection(new String[]{"MO","IA","IL","KY","TN","AR","OK","KA","NE"});
        clrMap.InsertIntoCollection(new String[]{"SD","ND","MN","IA","NE","WY","MT"});
        clrMap.InsertIntoCollection(new String[]{"IL","WI","IN","MO","IA"});
        clrMap.InsertIntoCollection(new String[]{"IL","WI","IN","MO","IA"});
        int cnt = clrMap.WhatExistState("WA");
        Assert.assertEquals (cnt,1);
        cnt = clrMap.WhatExistState("IL");
        Assert.assertEquals(cnt,2);
        cnt = clrMap.WhatExistState("CA");
        Assert.assertEquals(cnt,0);

    }

    @org.junit.Test
    public void isStateSelfNeighbors() {
        ColorMap clrMap = new ColorMap();
        clrMap.InsertIntoCollection(new String[]{"WA", "ID", "OR"});
        clrMap.InsertIntoCollection(new String[]{"ID", "MT", "WY", "UT", "NV", "OR", "ID"});
        clrMap.InsertIntoCollection(new String[]{"OR","NV","CA","WA"});
        clrMap.InsertIntoCollection(new String[]{"MT","ND","SD","WY","ID"});
        clrMap.InsertIntoCollection(new String[]{"ND","MN","SD","MT"});
        clrMap.InsertIntoCollection(new String[]{"MO","IA","IL","KY","TN","AR","OK","KA","NE"});
        clrMap.InsertIntoCollection(new String[]{"SD","ND","MN","IA","NE","WY","MT"});
        clrMap.InsertIntoCollection(new String[]{"IL","WI","IN","MO","IA"});
        clrMap.InsertIntoCollection(new String[]{"IL","WI","IN","MO","IA"});
        boolean res = clrMap.IsStateSelfNeighbors(clrMap.atlas.get(0));
        Assert.assertFalse(res);
        res = clrMap.IsStateSelfNeighbors(clrMap.atlas.get(1));
        Assert.assertTrue(res);
    }

    @org.junit.Test
    public void isExistNeighbors() {
        ColorMap clrMap = new ColorMap();
        clrMap.InsertIntoCollection(new String[]{"WA", "ID", "OR"});
        clrMap.InsertIntoCollection(new String[]{"ID", "MT", "WY", "UT", "NV", "OR"});
        clrMap.InsertIntoCollection(new String[]{"OR","NV","CA","WA"});
        clrMap.InsertIntoCollection(new String[]{"MT","ND","SD","WY","ID"});
        clrMap.InsertIntoCollection(new String[]{"ND","MN","SD","MT"});
        clrMap.InsertIntoCollection(new String[]{"MO","IA","IL","KY","TN","AR","OK","KA","NE"});
        clrMap.InsertIntoCollection(new String[]{"SD","ND","MN","IA","NE","WY","MT"});
        clrMap.InsertIntoCollection(new String[]{"IL","WI","IN","MO","IA"});
        clrMap.InsertIntoCollection(new String[]{"IL","WI","IN","MO","IA"});
        boolean res = clrMap.IsExistNeighbors(clrMap.atlas.get(0));
        Assert.assertTrue(res);
        res = clrMap.IsExistNeighbors(clrMap.atlas.get(1));
        Assert.assertFalse(res);
    }

    @org.junit.Test
    public void isCollectionValid() {
        ColorMap clrMap = new ColorMap();
        clrMap.InsertIntoCollection(new String[]{"WA", "ID", "OR"});
        clrMap.InsertIntoCollection(new String[]{"ID", "MT", "WA"});
        clrMap.InsertIntoCollection(new String[]{"OR","MT","WA"});
        clrMap.InsertIntoCollection(new String[]{"MT","OR","ID"});
        boolean res = clrMap.IsCollectionValid();
        Assert.assertTrue(res);
        clrMap.InsertIntoCollection(new String[]{"ND","MN","SD","MT"}); // нет MN, SD
        res = clrMap.IsCollectionValid();
        Assert.assertFalse(res);
        clrMap.atlas.remove(4);
        clrMap.InsertIntoCollection(new String[]{"ID", "MT", "WA"}); // повторяется ID
        res = clrMap.IsCollectionValid();
        Assert.assertFalse(res);
        clrMap.atlas.remove(4);
        clrMap.InsertIntoCollection(new String[]{"IL","WA","IL"}); // сам с собой сосед
        res = clrMap.IsCollectionValid();
        Assert.assertFalse(res);
    }

    @org.junit.Test
    public void existStateInNeighbors() {
        ColorMap clrMap = new ColorMap();
        clrMap.InsertIntoCollection(new String[]{"WA", "ID", "OR"});
        clrMap.InsertIntoCollection(new String[]{"ID", "MT", "WA"});
        clrMap.InsertIntoCollection(new String[]{"OR","MT","WA"});
        clrMap.InsertIntoCollection(new String[]{"MT","OR","ID"});
        boolean res = clrMap.ExistStateInNeighbors(clrMap.atlas.get(0));
        Assert.assertTrue(res);
        res = clrMap.ExistStateInNeighbors(clrMap.atlas.get(1));
        Assert.assertTrue(res);
        res = clrMap.ExistStateInNeighbors(clrMap.atlas.get(2));
        Assert.assertTrue(res);
        res = clrMap.ExistStateInNeighbors(clrMap.atlas.get(3));
        Assert.assertTrue(res);
        clrMap.atlas.remove(3);
        clrMap.InsertIntoCollection(new String[]{"MT","OR","IL"}); // нет IL
        res = clrMap.IsCollectionValid();
        Assert.assertFalse(res);
    }
}
