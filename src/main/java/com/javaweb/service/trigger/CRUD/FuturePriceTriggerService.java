    package com.javaweb.service.trigger.CRUD;

    import com.javaweb.dto.trigger.FuturePriceTriggerDTO;
    import com.javaweb.helpers.trigger.CheckSymbolExisted;
    import com.javaweb.helpers.trigger.TriggerMapHelper;
    import com.javaweb.model.trigger.FuturePriceTrigger;
    import com.javaweb.repository.FuturePriceTriggerRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    @Service
    public class FuturePriceTriggerService {
        @Autowired
        private FuturePriceTriggerRepository futurePriceTriggerRepository;

        @Autowired
        private TriggerMapHelper triggerMapHelper;

        @Autowired
        private CheckSymbolExisted checkSymbolExisted;

        public String createTrigger(FuturePriceTriggerDTO dto, String username) {
            if (checkSymbolExisted.symbolExistsInFuture(dto.getSymbol(), username)) {
                throw new IllegalArgumentException("Symbol already exists in database.");
            }
            FuturePriceTrigger trigger = triggerMapHelper.mapFuturePriceTrigger(dto);
            trigger.setUsername(username);

            FuturePriceTrigger savedTrigger = futurePriceTriggerRepository.save(trigger);
            return savedTrigger.getAlert_id();
        }
    }
