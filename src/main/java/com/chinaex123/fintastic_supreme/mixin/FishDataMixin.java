package com.chinaex123.fintastic_supreme.mixin;

import com.chinaex123.fintastic_supreme.FintasticSupreme;
import com.chinaex123.fintastic_supreme.data.FSDataComponents;
import com.li64.tide.data.fishing.CatchResult;
import com.li64.tide.data.fishing.FishingContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

/**
 * 鱼类数据 Mixin
 * <p>
 * 作用：在 Tide 模组生成钓鱼结果时，根据鱼的长度计算并添加重量数据组件
 */
@Mixin(targets = "com.li64.tide.data.fishing.FishData")
public class FishDataMixin {

    @Unique
    private static final Random fintastic_supreme$RANDOM = new Random();

    /**
     * 注入到 FishData.getResult() 方法的返回点
     * 在钓鱼结果生成后，为钓到的鱼添加重量数据
     * @param context 钓鱼上下文对象
     * @param cir 回调信息，用于获取和修改返回值
     */
    @Inject(method = "getResult", at = @At("RETURN"), remap = false)
    private void injectWeight(FishingContext context, CallbackInfoReturnable<CatchResult> cir) {
        try {
            // 获取钓鱼结果对象
            CatchResult result = cir.getReturnValue();
            var catchResultClass = result.getClass();

            // 通过反射从 CatchResult 中提取 ItemStack 物品堆
            ItemStack stack = null;
            for (var field : catchResultClass.getDeclaredFields()) {
                // 如果字段类型是 ItemStack，直接获取
                if (field.getType() == ItemStack.class) {
                    field.setAccessible(true);
                    stack = (ItemStack) field.get(result);
                    break;
                }
                // 如果字段类型是 List，尝试获取列表中的第一个 ItemStack
                else if (field.getType() == List.class) {
                    field.setAccessible(true);
                    List<?> items = (List<?>) field.get(result);
                    if (!items.isEmpty() && items.getFirst() instanceof ItemStack) {
                        stack = (ItemStack) items.getFirst();
                        break;
                    }
                }
            }

            // 如果未找到物品堆，直接返回
            if (stack == null) {
                return;
            }

            // 从注册表中获取 Tide 模组的鱼长度数据组件类型
            var lengthType = BuiltInRegistries.DATA_COMPONENT_TYPE.get(ResourceLocation.parse("tide:fish_length"));

            // 如果长度组件类型存在
            if (lengthType != null) {
                // 从物品堆的组件中获取长度数据
                Object lengthComponent = stack.getComponents().get(lengthType);

                // 如果长度数据是数字类型
                if (lengthComponent instanceof Number number) {
                    // 获取鱼的长度值（厘米）
                    double len = number.doubleValue();

                    /* 基础密度系数 */
                    double baseDensity = 0.001;
                    /* 计算体积：(长度/10)³ */
                    double volume = Math.pow(len / 10.0, 3.0);
                    /* 计算基础重量：体积 × 密度 × 1000 */
                    double weight = volume * baseDensity * 1000;
                    /* 生成随机系数：0.75-1.25 之间的随机值 */
                    double randomFactor = 0.75 + (fintastic_supreme$RANDOM.nextDouble() * 0.5);
                    /* 计算最终重量：基础重量 × 随机系数 */
                    double finalWeight = weight * randomFactor;

                    // 将计算出的重量设置到物品堆的重量数据组件中
                    stack.set(FSDataComponents.FISH_WEIGHT.get(), finalWeight);
                }
            }
        } catch (Exception e) {
            // 记录处理过程中的错误日志
            FintasticSupreme.LOGGER.error("[FishDataMixin] 处理过程中发生错误", e);
        }
    }
}
