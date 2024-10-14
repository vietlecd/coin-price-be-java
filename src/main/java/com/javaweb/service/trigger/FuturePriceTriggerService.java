    package com.javaweb.service.trigger;

    import com.javaweb.dto.trigger.FuturePriceTriggerDTO;
    import com.javaweb.helpers.trigger.CheckSymbolExisted;
    import com.javaweb.helpers.trigger.TriggerMapHelper;
    import com.javaweb.model.trigger.FuturePriceTrigger;
    import com.javaweb.repository.FuturePriceTriggerRepository;
    import com.javaweb.service.ITriggerService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    @Service
    public class FuturePriceTriggerService implements ITriggerService<FuturePriceTriggerDTO> {
        @Autowired
        private FuturePriceTriggerRepository futurePriceTriggerRepository;

        @Autowired
        private TriggerMapHelper triggerMapHelper;

        @Autowired
        private CheckSymbolExisted checkSymbolExisted;

        public String createTrigger(FuturePriceTriggerDTO dto) {
            if (checkSymbolExisted.symbolExistsInFuture(dto.getSymbol())) {
                throw new IllegalArgumentException("Symbol already exists in database.");
            }
            FuturePriceTrigger trigger = triggerMapHelper.mapFuturePriceTrigger(dto);
            FuturePriceTrigger savedTrigger = futurePriceTriggerRepository.save(trigger);
            return savedTrigger.getAlert_id();
        }
    }
