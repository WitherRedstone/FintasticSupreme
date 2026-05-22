//package com.chinaex123.fintastic_supreme.client;
//
//import com.chinaex123.fintastic_supreme.data.FSDataComponents;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Axis;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.model.HumanoidModel;
//import net.minecraft.client.player.AbstractClientPlayer;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
//import net.minecraft.client.renderer.entity.player.PlayerRenderer;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.HumanoidArm;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.ItemStack;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.client.event.RenderHandEvent;
//
//@EventBusSubscriber(value = Dist.CLIENT, modid = "fintastic_supreme")
//public class FishRender {
//
//    @SubscribeEvent
//    public static void onRenderHand(RenderHandEvent event) {
//        ItemStack stack = event.getItemStack();
//        Double weight = stack.get(FSDataComponents.FISH_WEIGHT.get());
//
//        if (weight == null || weight <= 0) {
//            return;
//        }
//
//        Minecraft mc = Minecraft.getInstance();
//        AbstractClientPlayer player = mc.player;
//        if (player == null) {
//            return;
//        }
//
//        event.setCanceled(true);
//
//        PoseStack poseStack = event.getPoseStack();
//        MultiBufferSource bufferSource = event.getMultiBufferSource();
//        int packedLight = event.getPackedLight();
//        float partialTicks = event.getPartialTick();
//
//        HumanoidArm mainArm = player.getMainArm();
//
//        // ========== 渲染鱼模型 ==========
//        poseStack.pushPose();
//        poseStack.mulPose(Axis.ZP.rotationDegrees(0)); // Z轴旋转
//        poseStack.mulPose(Axis.YP.rotationDegrees(-30));
//        poseStack.translate(-0.06, -0.2, -0.3); // 右手位置：X左右, Y上下, Z前后
//        poseStack.mulPose(Axis.XP.rotationDegrees(-90)); // X轴旋转
//        poseStack.scale(0.7f, 0.7f, 0.7f); // 鱼的缩放比例
//
//        try {
//            String fishId = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
//            EntityType<?> entityType = EntityType.byString("tide:" + fishId).orElse(null);
//
//            if (entityType != null && mc.level != null) {
//                LivingEntity entity = (LivingEntity) entityType.create(mc.level);
//                if (entity != null) {
//                    EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
//                    dispatcher.setRenderShadow(false);
//
//                    entity.yBodyRot = 0;
//                    entity.yHeadRot = 0;
//                    entity.hurtTime = 0;
//
//                    dispatcher.render(entity, 0.0, 0.0, 0.0, 0.0f, partialTicks, poseStack, bufferSource, packedLight);
//                    dispatcher.setRenderShadow(true);
//                }
//            }
//        } catch (Exception e) {
//            // Ignore
//        }
//
//        poseStack.popPose();
//
//        // ========== 渲染左手 ==========
////        poseStack.pushPose();
////        poseStack.translate(-0.2, -0.35, -0.15); // 左手位置
////        poseStack.mulPose(Axis.YP.rotationDegrees(-25)); // Y轴旋转
////        poseStack.mulPose(Axis.XP.rotationDegrees(-40)); // X轴旋转
////        poseStack.mulPose(Axis.ZP.rotationDegrees(-15)); // Z轴旋转
////        poseStack.scale(0.5f, 0.5f, 0.5f); // 手臂缩放比例
////        renderArm(mc, player, poseStack, bufferSource, packedLight, partialTicks, mainArm);
////        poseStack.popPose();
//
//        // ========== 渲染右手 ==========
//        poseStack.pushPose();
//        poseStack.translate(0.15, -0.2, -0.15); // 右手位置
//        poseStack.mulPose(Axis.YP.rotationDegrees(-20));  // Y轴旋转
//        poseStack.mulPose(Axis.XP.rotationDegrees(-40)); // X轴旋转
//        poseStack.mulPose(Axis.ZP.rotationDegrees(20));  // Z轴旋转
//        poseStack.scale(0.5f, 0.5f, 0.5f); // 手臂缩放比例
//        renderArm(mc, player, poseStack, bufferSource, packedLight, partialTicks, mainArm.getOpposite());
//        poseStack.popPose();
//    }
//
//    private static void renderArm(Minecraft mc, AbstractClientPlayer player, PoseStack poseStack,
//                                  MultiBufferSource bufferSource, int packedLight, float partialTicks,
//                                  HumanoidArm arm) {
//        PlayerRenderer renderer = (PlayerRenderer) mc.getEntityRenderDispatcher().getRenderer(player);
//        HumanoidModel<AbstractClientPlayer> model = renderer.getModel();
//
//        // 保存原始角度
//        float originalXRot, originalYRot, originalZRot;
//
//        if (arm == HumanoidArm.RIGHT) {
//            originalXRot = model.rightArm.xRot;
//            originalYRot = model.rightArm.yRot;
//            originalZRot = model.rightArm.zRot;
//
//            // 重置手臂角度为0，让 PoseStack 完全控制
//            model.rightArm.xRot = 0.0F;
//            model.rightArm.yRot = 0.0F;
//            model.rightArm.zRot = 0.0F;
//
//            // 渲染手臂基础层
//            model.rightArm.render(poseStack, bufferSource.getBuffer(
//                    RenderType.entityTranslucent(player.getSkin().texture())
//            ), 15728880, OverlayTexture.NO_OVERLAY);
//
//            // 恢复原始角度
//            model.rightArm.xRot = originalXRot;
//            model.rightArm.yRot = originalYRot;
//            model.rightArm.zRot = originalZRot;
//        } else {
//            originalXRot = model.leftArm.xRot;
//            originalYRot = model.leftArm.yRot;
//            originalZRot = model.leftArm.zRot;
//
//            // 重置手臂角度为0，让 PoseStack 完全控制
//            model.leftArm.xRot = 0.0F;
//            model.leftArm.yRot = 0.0F;
//            model.leftArm.zRot = 0.0F;
//
//            // 渲染手臂基础层
//            model.leftArm.render(poseStack, bufferSource.getBuffer(
//                    RenderType.entityTranslucent(player.getSkin().texture())
//            ), 15728880, OverlayTexture.NO_OVERLAY);
//
//            // 恢复原始角度
//            model.leftArm.xRot = originalXRot;
//            model.leftArm.yRot = originalYRot;
//            model.leftArm.zRot = originalZRot;
//        }
//    }
//}
