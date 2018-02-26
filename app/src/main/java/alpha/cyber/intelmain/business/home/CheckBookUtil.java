package alpha.cyber.intelmain.business.home;

import android.os.Message;

import com.rfid.api.GFunction;
import com.rfid.api.ISO14443AInterface;
import com.rfid.api.ISO14443ATag;
import com.rfid.api.ISO15693Interface;
import com.rfid.api.ISO15693Tag;
import com.rfid.def.ApiErrDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import alpha.cyber.intelmain.Constant;
import alpha.cyber.intelmain.VoicePlayer;
import alpha.cyber.intelmain.bean.InventoryReport;
import alpha.cyber.intelmain.util.Log;

/**
 * Created by wangrui on 2018/2/26.
 */

public class CheckBookUtil {

    public static String UiReadBlock(int blkAddr, int numOfBlksToRead,ISO15693Interface mTag) {
        if (blkAddr + numOfBlksToRead > 28) {// 数据块地址溢出
            numOfBlksToRead = 28 - blkAddr;
        }
        Integer numOfBlksRead = 0;
        Long bytesBlkDatRead = (long) 0;
        byte bufBlocks[] = new byte[4 * numOfBlksToRead];
        int iret = mTag.ISO15693_ReadMultiBlocks(false, blkAddr,
                numOfBlksToRead, numOfBlksRead, bufBlocks, bytesBlkDatRead);
        if (iret != ApiErrDefinition.NO_ERROR) {
            Log.e(Constant.TAG, "错误");
        }

        String strData = GFunction.encodeHexStr(bufBlocks);

        return strData;
    }

    public static void getInventoryList(CheckBookService pt, Message msg) {

        @SuppressWarnings("unchecked")
        Vector<Object> tagList = (Vector<Object>) msg.obj;
        if (pt.bRealShowTag && !pt.inventoryList.isEmpty()) {
            pt.inventoryList.clear();
        }
        if (!tagList.isEmpty() && pt.bBuzzer) {
            VoicePlayer.GetInst(pt).Play();
        }
        boolean b_find;
        for (int i = 0; i < tagList.size(); i++) {
            b_find = false;
            // ISO15693 TAG
            if (tagList.get(i) instanceof ISO15693Tag) {
                ISO15693Tag tagData = (ISO15693Tag) tagList.get(i);
                String uidStr = GFunction.encodeHexStr(tagData.uid);
                for (int j = 0; j < pt.inventoryList.size(); j++) {
                    InventoryReport mReport = pt.inventoryList.get(j);
                    if (mReport.getUidStr().equals(uidStr)) {
                        mReport.setFindCnt(mReport.getFindCnt() + 1);
                        b_find = true;
                        break;
                    }
                }
                if (!b_find) {
                    long mCnt = pt.bRealShowTag ? 0 : 1;
                    String tagName = ISO15693Interface
                            .GetTagNameById(tagData.tag_id);
                    pt.inventoryList.add(new InventoryReport(uidStr,
                            tagName, mCnt));

                }
            } else if (tagList.get(i) instanceof ISO14443ATag) {
                ISO14443ATag tagData = (ISO14443ATag) tagList.get(i);
                String uidStr = GFunction.encodeHexStr(tagData.uid);
                for (int j = 0; j < pt.inventoryList.size(); j++) {
                    InventoryReport mReport = pt.inventoryList.get(j);
                    if (mReport.getUidStr().equals(uidStr)) {
                        mReport.setFindCnt(mReport.getFindCnt() + 1);
                        b_find = true;
                        break;
                    }
                }
                if (!b_find) {
                    long mCnt = pt.bRealShowTag ? 0 : 1;
                    String tagName = ISO14443AInterface
                            .GetTagNameById(tagData.tag_id);
                    pt.inventoryList.add(new InventoryReport(uidStr,
                            tagName, mCnt));

                }
            }

        }
    }

}
