    package com.javaweb.service.trigger;

    import com.javaweb.dto.trigger.FuturePriceTriggerDTO;
    import com.javaweb.dto.trigger.SpotPriceTriggerDTO;
    import com.javaweb.helpers.trigger.TriggerHelper;
    import com.javaweb.model.trigger.FuturePriceTrigger;
    import com.javaweb.model.trigger.SpotPriceTrigger;
    import com.javaweb.repository.FuturePriceTriggerRepository;
    import com.javaweb.repository.SpotPriceTriggerRepository;
    import com.javaweb.service.ITriggerService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    @Service
    public class FuturePriceTriggerService implements ITriggerService<FuturePriceTriggerDTO> {
        @Autowired
        private FuturePriceTriggerRepository futurePriceTriggerRepository;

        @Autowired
        private TriggerHelper triggerHelper;

        public void createTrigger(FuturePriceTriggerDTO dto) {
            FuturePriceTrigger trigger = triggerHelper.mapFuturePriceTrigger(dto);
            futurePriceTriggerRepository.save(trigger);
        }
    }
