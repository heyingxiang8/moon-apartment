package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.model.entity.*;
import com.atguigu.lease.model.enums.ItemType;
import com.atguigu.lease.web.admin.mapper.*;
import com.atguigu.lease.web.admin.service.*;
import com.atguigu.lease.web.admin.vo.attr.AttrValueVo;
import com.atguigu.lease.web.admin.vo.graph.GraphVo;
import com.atguigu.lease.web.admin.vo.room.RoomDetailVo;
import com.atguigu.lease.web.admin.vo.room.RoomItemVo;
import com.atguigu.lease.web.admin.vo.room.RoomQueryVo;
import com.atguigu.lease.web.admin.vo.room.RoomSubmitVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {
    @Autowired
    private RoomAttrValueService roomAttrValueService;
    @Autowired
    private RoomFacilityService roomFacilityService;
    @Autowired
    private RoomLabelService roomLabelService;
    @Autowired
    private RoomPaymentTypeService roomPaymentTypeService;
    @Autowired
    private RoomLeaseTermService roomLeaseTermService;
    @Autowired
    private GraphInfoService graphInfoService;
    @Autowired
    private RoomInfoMapper roomInfoMapper;
    @Autowired
    private ApartmentInfoMapper appartmentInfoMapper;
    @Autowired
    private AttrValueMapper attrValueMapper;
    @Autowired
    private FacilityInfoMapper facilityInfoMapper;
    @Autowired
    private LabelInfoMapper labelInfoMapper;
    @Autowired
    private PaymentTypeMapper paymentTypeMapper;
    @Autowired
    private LeaseTermMapper leaseTermMapper;
    @Autowired
    private GraphInfoMapper graphInfoMapper;

    @Override
    public void saveOrUpdateRoom(RoomSubmitVo roomSubmitVo) {
        boolean isUpdate = roomSubmitVo.getId() != null;
        // 如果是更新，则原submitVo就是有id的；如果是插入，则mybatis-plus依旧会返回id并保存到submitVo中
        super.saveOrUpdate(roomSubmitVo);
        // 获取房间ID
        Long roomId = roomSubmitVo.getId();
        // 如果是更新情形
        if(isUpdate) {
            // 1. 删除属性信息列表
            LambdaQueryWrapper<RoomAttrValue> attrQueryWrapper = new LambdaQueryWrapper<>();
            attrQueryWrapper.eq(RoomAttrValue::getRoomId, roomId);
            roomAttrValueService.remove(attrQueryWrapper);

            // 2. 删除配套信息列表
            LambdaQueryWrapper<RoomFacility> facilityQueryWrapper = new LambdaQueryWrapper<>();
            facilityQueryWrapper.eq(RoomFacility::getRoomId, roomId);
            roomFacilityService.remove(facilityQueryWrapper);

            // 3. 删除标签信息列表
            LambdaQueryWrapper<RoomLabel> labelQueryWrapper = new LambdaQueryWrapper<>();
            labelQueryWrapper.eq(RoomLabel::getRoomId, roomId);
            roomLabelService.remove(labelQueryWrapper);

            // 4. 删除支付方式列表
            LambdaQueryWrapper<RoomPaymentType> paymentQueryWrapper = new LambdaQueryWrapper<>();
            paymentQueryWrapper.eq(RoomPaymentType::getRoomId, roomId);
            roomPaymentTypeService.remove(paymentQueryWrapper);

            // 5. 删除可选租期列表
            LambdaQueryWrapper<RoomLeaseTerm> termQueryWrapper = new LambdaQueryWrapper<>();
            termQueryWrapper.eq(RoomLeaseTerm::getRoomId, roomId);
            roomLeaseTermService.remove(termQueryWrapper);

            // 6. 删除图片列表
            LambdaQueryWrapper<GraphInfo> graphQueryWrapper = new LambdaQueryWrapper<>();
            graphQueryWrapper.eq(GraphInfo::getItemType, ItemType.ROOM);
            graphQueryWrapper.eq(GraphInfo::getItemId, roomId);
            graphInfoService.remove(graphQueryWrapper);
        }

        // 1. 保存新的属性信息列表
        List<Long> attrValueIdList = roomSubmitVo.getAttrValueIds();
        if(!CollectionUtils.isEmpty(attrValueIdList)){
            List<RoomAttrValue> roomAttrValueList = new ArrayList<>();
            for(Long attrValueId : attrValueIdList){
                RoomAttrValue roomAttrValue = RoomAttrValue.builder().roomId(roomId)
                                                .attrValueId(attrValueId).build();
                roomAttrValueList.add(roomAttrValue);
            }
            roomAttrValueService.saveBatch(roomAttrValueList);
        }
        // 2. 保存新的配套信息列表
        List<Long> facilityInfoIdList = roomSubmitVo.getFacilityInfoIds();
        if(!CollectionUtils.isEmpty(facilityInfoIdList)){
            List<RoomFacility> roomFacilityList = new ArrayList<>();
            for(Long facilityId : facilityInfoIdList){
                RoomFacility roomFacility = RoomFacility.builder().roomId(roomId)
                        .facilityId(facilityId).build();
                roomFacilityList.add(roomFacility);
            }
            roomFacilityService.saveBatch(roomFacilityList);
        }
        // 3. 保存新的标签信息列表
        List<Long> labelInfoIdList = roomSubmitVo.getLabelInfoIds();
        if(!CollectionUtils.isEmpty(labelInfoIdList)){
            List<RoomLabel> roomLabelList = new ArrayList<>();
            for(Long labelId : labelInfoIdList){
                RoomLabel roomLabel = RoomLabel.builder().roomId(roomId)
                        .labelId(labelId).build();
                roomLabelList.add(roomLabel);
            }
            roomLabelService.saveBatch(roomLabelList);
        }
        // 4. 保存新的支付方式列表
        List<Long> paymentTypeIdList = roomSubmitVo.getPaymentTypeIds();
        if(!CollectionUtils.isEmpty(paymentTypeIdList)){
            List<RoomPaymentType> roomPaymentTypeList = new ArrayList<>();
            for(Long paymentId : paymentTypeIdList){
                RoomPaymentType roomPaymentType = RoomPaymentType.builder().roomId(roomId)
                        .paymentTypeId(paymentId).build();
                roomPaymentTypeList.add(roomPaymentType);
            }
            roomPaymentTypeService.saveBatch(roomPaymentTypeList);
        }
        // 5. 保存新的可选租期列表
        List<Long> leaseTermIdList = roomSubmitVo.getLeaseTermIds();
        if(!CollectionUtils.isEmpty(leaseTermIdList)){
            List<RoomLeaseTerm> roomLeaseTermList = new ArrayList<>();
            for(Long termId : leaseTermIdList){
                RoomLeaseTerm roomLeaseTerm = RoomLeaseTerm.builder().roomId(roomId)
                        .leaseTermId(termId).build();
                roomLeaseTermList.add(roomLeaseTerm);
            }
            roomLeaseTermService.saveBatch(roomLeaseTermList);
        }
        // 6. 保存新的图片列表
        List<GraphVo> graphVoList = roomSubmitVo.getGraphVoList();
        if(!CollectionUtils.isEmpty(graphVoList)){
            List<GraphInfo> graphInfoList = new ArrayList<>();
            for(GraphVo graphVo : graphVoList){
                GraphInfo graphInfo = GraphInfo.builder().name(graphVo.getName())
                                        .itemType(ItemType.ROOM)
                                        .itemId(roomId)
                                        .url(graphVo.getUrl()).build();
                graphInfoList.add(graphInfo);
            }
            graphInfoService.saveBatch(graphInfoList);
        }
    }

    @Override
    public RoomDetailVo getRoomDetailById(Long id) {
        // 1. 查询RoomInfo
//        RoomInfo roomInfo = roomInfoMapper.selectById(id);
        RoomInfo roomInfo = super.getById(id);
        if (roomInfo == null) {
            return null;
        }

        // 2. 查询所有公寓信息
        ApartmentInfo apartmentInfo = appartmentInfoMapper.selectById(roomInfo.getApartmentId());

        // 3. 查询所有属性信息列表
        List<AttrValueVo> attrValueVoList = attrValueMapper.selectListByRoomId(id);

        // 4. 查询配套信息列表
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByRoomId(id);

        // 5. 查询标签信息列表
        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByRoomId(id);

        // 6. 查询支付方式列表
        List<PaymentType> paymentTypeList = paymentTypeMapper.selectListByRoomId(id);

        // 5. 查询可选租期列表
        List<LeaseTerm> leaseTermList = leaseTermMapper.selectListByRoomId(id);

        // 7. 查询图片列表
        List<GraphVo> graphVoList = graphInfoMapper.selectListByItemTypeAndId(ItemType.ROOM, id);

        RoomDetailVo adminRoomDetailVo = new RoomDetailVo();
        BeanUtils.copyProperties(roomInfo, adminRoomDetailVo);
        adminRoomDetailVo.setApartmentInfo(apartmentInfo);
        adminRoomDetailVo.setGraphVoList(graphVoList);
        adminRoomDetailVo.setAttrValueVoList(attrValueVoList);
        adminRoomDetailVo.setFacilityInfoList(facilityInfoList);
        adminRoomDetailVo.setLabelInfoList(labelInfoList);
        adminRoomDetailVo.setPaymentTypeList(paymentTypeList);
        adminRoomDetailVo.setLeaseTermList(leaseTermList);

        return adminRoomDetailVo;
    }

    @Override
    public void removeRoomById(Long id) {
        // 1. 删除RoomInfo
        super.removeById(id);

        // 2. 删除所有属性信息列表
        LambdaQueryWrapper<RoomAttrValue> attrQueryWrapper = new LambdaQueryWrapper<RoomAttrValue>();
        attrQueryWrapper.eq(RoomAttrValue::getRoomId, id);
        roomAttrValueService.remove(attrQueryWrapper);

        // 3. 删除配套信息列表
        LambdaQueryWrapper<RoomFacility> facilityQueryWrapper = new LambdaQueryWrapper<RoomFacility>();
        facilityQueryWrapper.eq(RoomFacility::getRoomId, id);
        roomFacilityService.remove(facilityQueryWrapper);

        // 4. 删除标签信息列表
        LambdaQueryWrapper<RoomLabel> labelQueryWrapper = new LambdaQueryWrapper<RoomLabel>();
        labelQueryWrapper.eq(RoomLabel::getRoomId, id);
        roomLabelService.remove(labelQueryWrapper);

        // 5. 删除支付方式列表
        LambdaQueryWrapper<RoomPaymentType> paymentQueryWrapper = new LambdaQueryWrapper<RoomPaymentType>();
        paymentQueryWrapper.eq(RoomPaymentType::getRoomId, id);
        roomPaymentTypeService.remove(paymentQueryWrapper);

        // 6. 删除可选租期列表
        LambdaQueryWrapper<RoomLeaseTerm> leaseQueryWrapper = new LambdaQueryWrapper<RoomLeaseTerm>();
        leaseQueryWrapper.eq(RoomLeaseTerm::getRoomId, id);
        roomLeaseTermService.remove(leaseQueryWrapper);

        // 7. 删除图片列表
        LambdaQueryWrapper<GraphInfo> graphQueryWrapper = new LambdaQueryWrapper<GraphInfo>();
        graphQueryWrapper.eq(GraphInfo::getItemType, ItemType.ROOM);
        graphQueryWrapper.eq(GraphInfo::getItemId, id);
        graphInfoService.remove(graphQueryWrapper);
    }

    @Override
    public IPage<RoomItemVo> pageRoomItemByQuery(IPage<RoomItemVo> page, RoomQueryVo queryVo) {
        return roomInfoMapper.pageRoomItemByQuery(page, queryVo);
    }
}




