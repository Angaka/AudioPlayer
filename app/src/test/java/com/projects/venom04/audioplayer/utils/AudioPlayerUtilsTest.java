package com.projects.venom04.audioplayer.utils;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Venom on 27/09/2017.
 */
public class AudioPlayerUtilsTest {
    @Test
    public void isLatin() throws Exception {
        boolean isEqualKorean = AudioPlayerUtils.isLatin("안녕 세상");
        Assert.assertEquals(false, isEqualKorean);

        boolean isEqualGreek = AudioPlayerUtils.isLatin("Γειά σου Κόσμε");
        Assert.assertEquals(false, isEqualGreek);
    }
}